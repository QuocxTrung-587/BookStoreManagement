package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.dto.TaoPhieuNhapDTO;
import vn.tdtu.finalterm.service.factory.PhieuFactoryProvider;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ChiTietPNService {
    @Autowired
    ChiTietPNRepository chiTietPNRepository;
    @Autowired
    ChiNhanhRepository chiNhanhRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    PhieuNhapRepository phieuNhapRepository;
    @Autowired
    QuanLySPService quanLySPService;
    @Autowired
    private PhieuFactoryProvider phieuFactoryProvider;

    public ResponseEntity<ResponseObject> findAllChiTietPN() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiTietPN Success", chiTietPNRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllChiTietPNByChiNhanhId(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find Chi Nhanh with id = " + chiNhanhId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiTietPN By ChiNhanh Success", chiTietPNRepository.findAllByChiNhanh(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> findAllChiTietPNBySanPhamId(Long sanPhamId) {
        Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);

        if(!sanPham.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("ok", "Can't find SanPham with id = " + sanPhamId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All PhieuNhap By SanPhamId Success", chiTietPNRepository.findAllBySanPham(sanPham))
        );
    }

    public ResponseEntity<ResponseObject> findAllChiTietPNByCNIdAndSPId(Long chiNhanhId, Long sanPhamId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);
        Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find ChiNhanh with id = " + chiNhanhId, "")
        );
        if(!sanPham.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find SanPham with id = " + sanPhamId, "")
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiTietPN By ChiNhanh And SanPham Success", chiTietPNRepository.findAllByChiNhanhAndSanPham(chiNhanh, sanPham))
        );
    }

    public ResponseEntity<ResponseObject> insertChiTietPNAndPNAndQLSP(TaoPhieuNhapDTO taoPhieuNhapDTO) {
        List<SanPham> boxSanPham = new ArrayList<>();

        // Check All SanPham Id
        for(Long sanPhamId : taoPhieuNhapDTO.getSanPhamId()) {
            Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);
            if(!sanPham.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find SanPham with id = " + sanPhamId, "")
                );
            } else boxSanPham.add(sanPham.get());
        }

        // Check Chi Nhanh Id
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(taoPhieuNhapDTO.getChiNhanhId());
        if(!chiNhanh.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find ChiNhanh with id = " + taoPhieuNhapDTO.getChiNhanhId(), "")
            );
        }

        // Sử dụng factory để tạo phiếu nhập
        PhieuNhap phieuNhap = (PhieuNhap) phieuFactoryProvider.getFactory("phieunhap")
                                                             .createPhieu(chiNhanh.get());

        // Add PhieuNhap
        phieuNhap.setChiNhanh(chiNhanh.get());
        PhieuNhap phieuNhapAfterSave = phieuNhapRepository.save(phieuNhap);

        // Find PhieuNhap just added
        Optional<PhieuNhap> phieuNhapOptional = phieuNhapRepository.findById(phieuNhapAfterSave.getId());

        int indexBox = 0;
        for(ChiTietPhieuNhap chiTietPhieuNhap : taoPhieuNhapDTO.getChiTietPhieuNhap()) {
            if(boxSanPham != null) {
                chiTietPhieuNhap.setSanPham(boxSanPham.get(indexBox));
                chiTietPhieuNhap.setPhieuNhap(phieuNhapOptional.get());
                chiTietPhieuNhap.setChiNhanh(chiNhanh.get());
            }
            indexBox++;
        }

        // Take tongTien from ChiTietPhieuNhap just saved and sum them for tongCong of PhieuNhap
        List<ChiTietPhieuNhap> chiTietPhieuNhapList = chiTietPNRepository.saveAll(Arrays.stream(taoPhieuNhapDTO.getChiTietPhieuNhap()).toList());
        float sumTongTien = 0.0f;
        for(ChiTietPhieuNhap chiTietPhieuNhap : chiTietPhieuNhapList) {
            sumTongTien += chiTietPhieuNhap.getTongTien();
        }

        // Assign TongCong
        phieuNhapOptional.get().setTongCong(sumTongTien);
        PhieuNhap resPhieuNhap = phieuNhapRepository.save(phieuNhapOptional.get());

        // Add QuanlySanPham
        quanLySPService.insertQuanLySP(resPhieuNhap.getNgayNhap(), chiTietPhieuNhapList);

        // Update All TrangThai of QuanLySP
        quanLySPService.updateAllTrangThai();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert ChiTietPhieuNhap and PhieuNhap Success", chiTietPhieuNhapList)
        );
    }

    public ResponseEntity<ResponseObject> updateChiTietPN(ChiTietPhieuNhap chiTietPhieuNhap, Long chiTietPNId) {
        Optional<ChiTietPhieuNhap> foundCTPN = chiTietPNRepository.findById(chiTietPNId);

        if(!foundCTPN.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find ChiTietPhieuNhap with id = " + chiTietPNId, "")
            );
        }

        // Take previous soLuong for QLSP
        int soLuongOld = foundCTPN.get().getSoLuong();

        // Find PhieuNhap to Update TongCong (Get Old TongTien and subtract it)
        Optional<PhieuNhap> phieuNhap = phieuNhapRepository.findById(foundCTPN.get().getPhieuNhap().getId());
        phieuNhap.get().setTongCong(phieuNhap.get().getTongCong() - foundCTPN.get().getTongTien());

        ChiTietPhieuNhap updatedCTPN = foundCTPN
                .map(CTPN -> {
                    CTPN.setGiaNhap(chiTietPhieuNhap.getGiaNhap());
                    CTPN.setSoLuong(chiTietPhieuNhap.getSoLuong());

                    return chiTietPNRepository.save(CTPN);
                }).orElseGet(() -> {
                    return null;
                });

        // Update Phieu Nhap
        phieuNhap.get().setTongCong(phieuNhap.get().getTongCong() + updatedCTPN.getTongTien());
        phieuNhapRepository.save(phieuNhap.get());

        // Update QuanLySP
        quanLySPService.updateTrongKhoSameSoLuong(foundCTPN.get().getSanPham(), foundCTPN.get().getChiNhanh(), soLuongOld, updatedCTPN.getSoLuong());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update ChiTietPN, PhieuNhap, QuanLySP Success", updatedCTPN)
        );
    }

    public ResponseEntity<ResponseObject> deleteChiTietPN(Long chiTietPNId) {
        Optional<ChiTietPhieuNhap> foundCTPN = chiTietPNRepository.findById(chiTietPNId);

        if(!foundCTPN.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find ChiTietPhieuNhap with id = " + chiTietPNId, "")
            );
        }

        // Delete Quantity for QuanLySP from ChiTietPN
        List<ChiTietPhieuNhap> termCTPN = new ArrayList<>();
        termCTPN.add(foundCTPN.get());
        ResponseEntity<ResponseObject> checkQuantity = quanLySPService.updateEffectByDeleteChiTietPN(termCTPN);
        if(checkQuantity != null) {
            return checkQuantity;
        }

        // Find PhieuNhap to Update TongCong (Get Old TongTien and subtract it)
        Optional<PhieuNhap> phieuNhap = phieuNhapRepository.findById(foundCTPN.get().getPhieuNhap().getId());
        phieuNhap.get().setTongCong(phieuNhap.get().getTongCong() - foundCTPN.get().getTongTien());
        phieuNhapRepository.save(phieuNhap.get());

        // Delete chiTietPN
        chiTietPNRepository.deleteById(chiTietPNId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete ChiTietPhieuNhap Success", "")
        );
    }
}
