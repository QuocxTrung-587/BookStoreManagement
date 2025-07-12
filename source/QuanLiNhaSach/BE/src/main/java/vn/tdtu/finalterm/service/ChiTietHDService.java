package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.dto.TaoHoaDonDTO;
import vn.tdtu.finalterm.service.factory.PhieuFactoryProvider;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.ChiTietHDRepository;
import vn.tdtu.finalterm.repositories.HoaDonRepository;
import vn.tdtu.finalterm.repositories.SanPhamRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ChiTietHDService implements SearchStrategy {
    @Autowired
    ChiTietHDRepository chiTietHDRepository;
    @Autowired
    ChiNhanhRepository chiNhanhRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    QuanLySPService quanLySPService;
    @Autowired
    private PhieuFactoryProvider phieuFactoryProvider;

    public ResponseEntity<ResponseObject> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiTietHD Success", chiTietHDRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllChiTietHDByChiNhanhId(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find Chi Nhanh with id = " + chiNhanhId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiTietHD By ChiNhanh Success", chiTietHDRepository.findAllByChiNhanh(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> findAllChiTietHDByCNIdAndSPId(Long chiNhanhId, Long sanPhamId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);
        Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find ChiNhanh with id = " + chiNhanhId, "")
        );
        if(!sanPham.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find SanPham with id = " + sanPhamId, "")
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiTietHD By ChiNhanh And SanPham Success", chiTietHDRepository.findAllByChiNhanhAndSanPham(chiNhanh, sanPham))
        );
    }

    public ResponseEntity<ResponseObject> insertChiTietHDAndHD(TaoHoaDonDTO taoHoaDonDTO) {
        List<SanPham> boxSanPham = new ArrayList<>();

        for(Long sanPhamId : taoHoaDonDTO.getSanPhamId()) {
            Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);
            if(!sanPham.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Can't find SanPham with id = " + sanPhamId, "")
                );
            } else boxSanPham.add(sanPham.get());
        }

        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(taoHoaDonDTO.getChiNhanhId());
        if(!chiNhanh.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find ChiNhanh with id = " + taoHoaDonDTO.getChiNhanhId(), "")
            );
        }

        // Sử dụng factory để tạo hóa đơn
        HoaDon hoaDon = (HoaDon) phieuFactoryProvider.getFactory("hoadon")
                                                    .createPhieu(chiNhanh.get());
        
        // Sell SP (delete quantity for QuanLySP)
        ResponseEntity<ResponseObject> checkQuantity = quanLySPService.deleteQuantityQuanLySP(
            taoHoaDonDTO.getSanPhamId(), 
            taoHoaDonDTO.getChiNhanhId(), 
            taoHoaDonDTO.getChiTietHoaDon()
        );
        
        if(checkQuantity != null) {
            return checkQuantity;
        }

        // Save HoaDon
        HoaDon hoaDonAfterSave = hoaDonRepository.save(hoaDon);

        // Find HoaDon just added
        Optional<HoaDon> savedHoaDon = hoaDonRepository.findById(hoaDonAfterSave.getId());

        // Process ChiTietHoaDon
        int indexBox = 0;
        for(ChiTietHoaDon chiTietHoaDon : taoHoaDonDTO.getChiTietHoaDon()) {
            if(boxSanPham != null) {
                chiTietHoaDon.setSanPham(boxSanPham.get(indexBox));
                chiTietHoaDon.setHoaDon(savedHoaDon.get());
                chiTietHoaDon.setChiNhanh(chiNhanh.get());
            }
            indexBox++;
        }

        // Calculate and update tongCong
        List<ChiTietHoaDon> chiTietHoaDonList = chiTietHDRepository.saveAll(
            Arrays.stream(taoHoaDonDTO.getChiTietHoaDon()).toList()
        );
        
        float sumTongTien = 0.0f;
        for(ChiTietHoaDon chiTietHoaDon : chiTietHoaDonList) {
            sumTongTien += chiTietHoaDon.getTongTien();
        }

        savedHoaDon.get().setTongCong(sumTongTien);
        HoaDon resHoaDon = hoaDonRepository.save(savedHoaDon.get());

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Insert ChiTietHoaDon and HoaDon Success", chiTietHoaDonList)
        );
    }

    public ResponseEntity<ResponseObject> updateChiTietHD(ChiTietHoaDon chiTietHoaDon, Long chiTietHDId) {
        Optional<ChiTietHoaDon> foundCTHD = chiTietHDRepository.findById(chiTietHDId);

        if(!foundCTHD.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find ChiTietHoaDon with id = " + chiTietHDId, "")
            );
        }

        // Update for quantity of QuanLySP
        int newSoLuong = chiTietHoaDon.getSoLuong() - foundCTHD.get().getSoLuong();
        ChiTietHoaDon objCTHD = new ChiTietHoaDon(null, 0, newSoLuong, 0, null, null, null);
        ResponseEntity<ResponseObject> checkQuantity = quanLySPService.deleteQuantityQuanLySP(new Long[]{foundCTHD.get().getSanPham().getId()}, foundCTHD.get().getChiNhanh().getId(), new ChiTietHoaDon[]{objCTHD});
        if(checkQuantity != null) {
            return checkQuantity;
        }

        // Find HoaDon to Update TongCong (Get Old TongTien and subtract it)
        Optional<HoaDon> hoaDon = hoaDonRepository.findById(foundCTHD.get().getHoaDon().getId());
        hoaDon.get().setTongCong(hoaDon.get().getTongCong() - foundCTHD.get().getTongTien());

        ChiTietHoaDon updatedCTHD = foundCTHD
                .map(CTHD -> {
                    CTHD.setGiaBan(chiTietHoaDon.getGiaBan());
                    CTHD.setSoLuong(chiTietHoaDon.getSoLuong());

                    return chiTietHDRepository.save(CTHD);
                }).orElseGet(() -> {
                    return null;
                });

        // Update HoaDon
        hoaDon.get().setTongCong(hoaDon.get().getTongCong() + updatedCTHD.getTongTien());
        hoaDonRepository.save(hoaDon.get());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update ChiTietHD, HoaDon Success", updatedCTHD)
        );
    }

    public ResponseEntity<ResponseObject> deleteChiTietHD(Long chiTietHDId) {
        Optional<ChiTietHoaDon> foundCTHD = chiTietHDRepository.findById(chiTietHDId);

        if(!foundCTHD.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find ChiTietHoaDon with id = " + chiTietHDId, "")
            );
        }

        // Update for quantity of QuanLySP
        ChiTietHoaDon objCTHD = new ChiTietHoaDon(null, 0, foundCTHD.get().getSoLuong()*-1, 0, null, null, null);
        ResponseEntity<ResponseObject> checkQuantity = quanLySPService.deleteQuantityQuanLySP(new Long[]{foundCTHD.get().getSanPham().getId()}, foundCTHD.get().getChiNhanh().getId(), new ChiTietHoaDon[]{objCTHD});
        if(checkQuantity != null) {
            return checkQuantity;
        }

        // Find HoaDon to Update TongCong (Get Old TongTien and subtract it)
        Optional<HoaDon> hoaDon = hoaDonRepository.findById(foundCTHD.get().getHoaDon().getId());
        hoaDon.get().setTongCong(hoaDon.get().getTongCong() - foundCTHD.get().getTongTien());
        hoaDonRepository.save(hoaDon.get());

        // Delete chiTietHD
        chiTietHDRepository.deleteById(chiTietHDId);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete ChiTietHoaDon Success", "")
        );
    }
}
