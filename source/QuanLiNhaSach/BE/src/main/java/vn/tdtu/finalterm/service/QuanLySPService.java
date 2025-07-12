package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.enums.DefaultValues;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.QuanLySPRepository;
import vn.tdtu.finalterm.repositories.SanPhamRepository;
import vn.tdtu.finalterm.repositories.ChiTietHDRepository;
import vn.tdtu.finalterm.observer.EventListener;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class QuanLySPService implements EventListener {
    @Autowired
    QuanLySPRepository quanLySPRepository;
    @Autowired
    ChiNhanhRepository chiNhanhRepository;
    @Autowired
    SanPhamRepository sanPhamRepository;
    @Autowired
    HoaDonService hoaDonService;
    public QuanLySPService(HoaDonService hoaDonService) {
        this.hoaDonService = hoaDonService;
        hoaDonService.addListener(this);
    }
    @Override
    public void update (String eventType, Object data){
        if (eventType.equals("HOADON_CREATED") && data instanceof HoaDon) {
            HoaDon hoaDon = (HoaDon) data;
            System.out.println("Hóa đơn mới được tạo: " + hoaDon.getId() + ". Cập nhật kho...");

        } else if (eventType.equals("HOADON_DELETED")) {
            System.out.println("Hóa đơn bị xóa: " + ((HoaDon) data).getId() + ". Khôi phục kho...");
        }
    }

    public ResponseEntity<ResponseObject> findAllQuanLySP() {
        updateAllTrangThai();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLySanPham Success", quanLySPRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllQuanLySPByChiNhanh(ChiNhanh chiNhanh) {
        updateAllTrangThai();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLySanPham By ChiNhanh Success", quanLySPRepository.findAllByMaCN(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> findAllQuanLySPByChiNhanhId(Long chiNhanhId) {
        updateAllTrangThai();

        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with id = " + chiNhanhId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLySanPham By ChiNhanh Success", quanLySPRepository.findAllByMaCN(chiNhanh.get()))
        );
    }

    public ResponseEntity<ResponseObject> findAllQuanLySPBySanPhamId(Long sanPhamId) {
        updateAllTrangThai();

        Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);

        if(!sanPham.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find SanPham with id = " + sanPhamId, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLySanPham By SanPham Success", quanLySPRepository.findAllByMaSP(sanPham.get()))
        );
    }

    public ResponseEntity<ResponseObject> findAllQLSPByCNIdAndSPId(Long chiNhanhId, Long sanPhamId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);
        Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find ChiNhanh with id = " + chiNhanhId, "")
        );
        if(!sanPham.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find SanPham with id = " + sanPhamId, "")
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLySanPham By ChiNhanh And SanPham Success", quanLySPRepository.findAllByMaCNAndMaSP(chiNhanh, sanPham))
        );
    }

    public ResponseEntity<ResponseObject> findQuanLySPByTenCN(String name) {
        updateAllTrangThai();

        List<ChiNhanh> chiNhanh = chiNhanhRepository.findByTenChiNhanhContains(name);

        if(chiNhanh.size() <= 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with tenChiNhanh like " + name, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All QuanLySanPham Success", quanLySPRepository.findAllByMaCNIn(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> findQLSPThatExpired() {
        updateAllTrangThai();

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query QuanLySanPham Success", quanLySPRepository.findAllByTrangThai(0))
        );
    }

    public void insertQuanLySP(Date ngayNhap, List<ChiTietPhieuNhap> chiTietPhieuNhapList) {
        LocalDate date = ngayNhap.toLocalDate().plusDays((long) DefaultValues.KYHAN.getValue());
        Date hanTon = Date.valueOf(date);

        for(ChiTietPhieuNhap item : chiTietPhieuNhapList) {
            QuanLySanPham updatedQLSP = quanLySPRepository.findByMaSPAndMaCN(item.getSanPham(), item.getChiNhanh())
                    .map(QLSP -> {
                        QLSP.setGiaBan(item.getGiaNhap() * DefaultValues.HESOGIATANG.getValue());

                        int sumSoLuong = QLSP.getTrongKho() + item.getSoLuong();
                        QLSP.setTrongKho(sumSoLuong);

                        QLSP.setTongHang(sumSoLuong + QLSP.getTrenKe());

                        QLSP.setHanTon(hanTon);

                        QLSP.setTrangThai(1);

                        return quanLySPRepository.save(QLSP);
                    }).orElseGet(() -> {
                        float giaBan = item.getGiaNhap() * DefaultValues.HESOGIATANG.getValue();

                        QuanLySanPham quanLySanPham = new QuanLySanPham(
                                null,
                                hanTon,
                                giaBan,
                                0,
                                item.getSoLuong(),
                                item .getSoLuong(),
                                1,
                                item.getSanPham(),
                                item.getChiNhanh());

                        return quanLySPRepository.save(quanLySanPham);
                    });
        }
    }

    public void updateTrongKhoSameSoLuong(SanPham sanPham, ChiNhanh chiNhanh, int soLuongOld, int soLuongNew) {
        QuanLySanPham quanLySanPham = quanLySPRepository.findByMaSPAndMaCN(sanPham, chiNhanh)
                .map(QLSP -> {
                    QLSP.setTrongKho(QLSP.getTrongKho() - soLuongOld + soLuongNew);

                    return quanLySPRepository.save(QLSP);
                }).orElseGet(() -> {
                    return null;
                });
    }

    public ResponseEntity<ResponseObject> updateQuanLySP(QuanLySanPham quanLySanPham, Long quanLySPId) {
        QuanLySanPham updateQuanLySanPham = quanLySPRepository.findById(quanLySPId)
                .map((QLSP) -> {
                    QLSP.setGiaBan(quanLySanPham.getGiaBan());

                    return quanLySPRepository.save(QLSP);
                }).orElseGet(() -> {
                    return null;
                });

        if(updateQuanLySanPham == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find QuanLySanPham with id = " + quanLySPId, "")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update QuanLySanPham Success", updateQuanLySanPham)
        );
    }

    public ResponseEntity<ResponseObject> moveSPInSameCN(QuanLySanPham quanLySanPham, Long quanLySPId) {
        Optional<QuanLySanPham> foundQuanLySanPham = quanLySPRepository.findById(quanLySPId);

        if(!foundQuanLySanPham.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find QuanLySanPham with id = " + quanLySPId, "")
            );
        }

        if(quanLySanPham.getTrenKe() > 0) {
            // Valid trenKe
            if(quanLySanPham.getTrenKe() > foundQuanLySanPham.get().getTrongKho()) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Can't move with quantity > trongKho", "")
                );
            }

            foundQuanLySanPham.get().setTrenKe(foundQuanLySanPham.get().getTrenKe() + quanLySanPham.getTrenKe());
            foundQuanLySanPham.get().setTrongKho(foundQuanLySanPham.get().getTrongKho() - quanLySanPham.getTrenKe());

        } else if(quanLySanPham.getTrongKho() > 0) {
            // Valid trongKho
            if(quanLySanPham.getTrongKho() > foundQuanLySanPham.get().getTrenKe()) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Can't move with quantity > trenKe", "")
                );
            }

            foundQuanLySanPham.get().setTrongKho(foundQuanLySanPham.get().getTrongKho() + quanLySanPham.getTrongKho());
            foundQuanLySanPham.get().setTrenKe(foundQuanLySanPham.get().getTrenKe() - quanLySanPham.getTrongKho());
        }

        else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Can't move with negative quantity", "")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update QuanLySanPham Success", quanLySPRepository.save(foundQuanLySanPham.get()))
        );
    }

    public ResponseEntity<ResponseObject> updateAllTrangThai() {
        List<QuanLySanPham> boxQuanLySanPham = quanLySPRepository.findAll();
        if(boxQuanLySanPham.size() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find any QuanLySanPham", "")
            );
        }

        for(QuanLySanPham quanLySanPham : boxQuanLySanPham) {
            if(quanLySanPham.getHanTon().before(new java.util.Date())) {
                quanLySanPham.setTrangThai(0);
            } else quanLySanPham.setTrangThai(1);
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update TrangThai Success", quanLySPRepository.saveAll(boxQuanLySanPham))
        );
    }

    public ResponseEntity<ResponseObject> updateEffectByDeleteChiTietPN(List<ChiTietPhieuNhap> chiTietPhieuNhapList) {
        List<QuanLySanPham> updatedQLSP = new ArrayList<>();
        List<Long> boxId = new ArrayList<>();

        for(ChiTietPhieuNhap chiTietPhieuNhap : chiTietPhieuNhapList) {
            QuanLySanPham quanLySanPham = quanLySPRepository.findByMaSPAndMaCN(chiTietPhieuNhap.getSanPham(), chiTietPhieuNhap.getChiNhanh())
                    .map(QLSP -> {
                        int sLConLaiTrenKe = QLSP.getTrenKe() - chiTietPhieuNhap.getSoLuong();
                        if(sLConLaiTrenKe > 0) {
                            QLSP.setTrenKe(sLConLaiTrenKe);
                        } else if(sLConLaiTrenKe == 0) {
                            QLSP.setTrenKe(0);
                        } else {
                            QLSP.setTrenKe(0);

                            int sLConLaiTrongKho = QLSP.getTrongKho() + sLConLaiTrenKe;

                            if (sLConLaiTrongKho > 0) {
                                QLSP.setTrongKho(sLConLaiTrongKho);
                            } else if (sLConLaiTrongKho == 0) {
                                QLSP.setTrongKho(0);
                            } else {
                                // TrongKho is Negative
                                boxId.add(QLSP.getId());
                                return null;
                            }
                        }

                        return QLSP;
                    }).orElseGet(() -> {
                        return null;
                    });

            updatedQLSP.add(quanLySanPham);
        }

        for(QuanLySanPham item : updatedQLSP) {
            if(item == null) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "There aren't enough quantity for QuanLySanPham with id = " + boxId.get(0), "")
                );
            }
        }
        quanLySPRepository.saveAll(updatedQLSP);
        return null;
    }

    public ResponseEntity<ResponseObject> deleteQuantityQuanLySP(Long[] sanPhamId, Long chiNhanhId, ChiTietHoaDon[] chiTietHoaDonList) {
        AtomicBoolean checkQuantity = new AtomicBoolean(false);

//        ChiNhanh objCN = new ChiNhanh(chiNhanhId, null, null, null, null, null, null, null, null);
        int indexCTHD = 0;
        List<QuanLySanPham> boxQLSP = new ArrayList<>();
        for(Long id : sanPhamId) {
//            SanPham objSP = new SanPham(id, null, null, null, null, null, null, null, null);
            Optional<QuanLySanPham> quanLySanPham = quanLySPRepository.findByMaSPIdAndMaCNId(id, chiNhanhId);

            if(quanLySanPham.isPresent()) {
                QuanLySanPham QLSP = quanLySanPham.get();

                int sLConLaiTrenKe = QLSP.getTrenKe() - chiTietHoaDonList[indexCTHD].getSoLuong();
                if(sLConLaiTrenKe > 0) {
                    QLSP.setTrenKe(sLConLaiTrenKe);
                } else if(sLConLaiTrenKe == 0) {
                    QLSP.setTrenKe(0);
                } else {
                    QLSP.setTrenKe(0);

                    int sLConLaiTrongKho = QLSP.getTrongKho() + sLConLaiTrenKe;

                    if (sLConLaiTrongKho > 0) {
                        QLSP.setTrongKho(sLConLaiTrongKho);
                    } else if (sLConLaiTrongKho == 0) {
                        QLSP.setTrongKho(0);
                    } else {
                        // trongKho is negative
                        checkQuantity.set(true);
                    }
                }

                boxQLSP.add(QLSP);
            } else {
                checkQuantity.set(true);
            }

            if(checkQuantity.get()) break;
            indexCTHD++;
        }
        if(checkQuantity.get()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "There aren't enough quantity for SanPham with id = " + sanPhamId[indexCTHD], "")
            );
        }
        quanLySPRepository.saveAll(boxQLSP);
        return null;
    }

    public ResponseEntity<ResponseObject> checkStockAvailability(Long[] sanPhamId, Long chiNhanhId, ChiTietHoaDon[] chiTietHoaDonList) {
        AtomicBoolean checkQuantity = new AtomicBoolean(false);
        int indexCTHD = 0;

        for(Long id : sanPhamId) {
            Optional<QuanLySanPham> quanLySanPham = quanLySPRepository.findByMaSPIdAndMaCNId(id, chiNhanhId);

            if(quanLySanPham.isPresent()) {
                QuanLySanPham QLSP = quanLySanPham.get();

                if(QLSP.getTrenKe() < chiTietHoaDonList[indexCTHD].getSoLuong()) {
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                            new ResponseObject("failed", "Không đủ số lượng sản phẩm trên kệ cho sản phẩm có id = " + id, "")
                    );
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Không tìm thấy sản phẩm với id = " + id, "")
                );
            }
            indexCTHD++;
        }

        return null;
    }
}
