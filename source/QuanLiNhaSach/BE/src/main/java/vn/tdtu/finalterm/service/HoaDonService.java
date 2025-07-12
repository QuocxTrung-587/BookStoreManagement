package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.service.factory.PhieuFactoryProvider;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.ChiTietHDRepository;
import vn.tdtu.finalterm.repositories.HoaDonRepository;
import vn.tdtu.finalterm.service.iterator.ChiTietHDList;
import vn.tdtu.finalterm.service.iterator.ChiTietHoaDonCollection;
import vn.tdtu.finalterm.service.iterator.ChiTietHoaDonIterator;

import vn.tdtu.finalterm.observer.EventListener;
import vn.tdtu.finalterm.observer.EventSubject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class HoaDonService implements EventSubject {
    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private PhieuFactoryProvider phieuFactoryProvider;

    @Autowired
    private ChiNhanhRepository chiNhanhRepository;
    @Autowired
    ChiTietHDRepository chiTietHDRepository;
    @Autowired
    QuanLySPService quanLySPService;

    private List<EventListener> listeners = new ArrayList<>();

    @Override
    public void addListener(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyListeners(String eventType, Object data) {
        for (EventListener listener : listeners) {
            listener.update(eventType, data);
        }
    }


    public ResponseEntity<ResponseObject> findAllHoaDon() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All HoaDon Success", hoaDonRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findAllHDByChiNhanhId(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        if(!chiNhanh.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("ok", "Can't find ChiNhanh with id = " + chiNhanhId, "")
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All HoaDon By ChiNhanhId Success", hoaDonRepository.findAllByChiNhanh(chiNhanh))
        );
    }

    public ResponseEntity<ResponseObject> findHDByTenChiNhanh(String key) {
        List<ChiNhanh> chiNhanhList = chiNhanhRepository.findByTenChiNhanhContains(key);

        if(chiNhanhList.size() <= 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with tenChiNhanh like " + key, "")
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All HoaDon Success", hoaDonRepository.findAllByChiNhanhIn(chiNhanhList))
        );
    }

    public ResponseEntity<ResponseObject> findHDByNgayLap(HoaDon hoaDon) {
        List<HoaDon> hoaDons = hoaDonRepository.findByNgayLap(hoaDon.getNgayLap());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All HoaDon By NgayLap Success", hoaDons)
        );
    }

    public ResponseEntity<ResponseObject> updateHoaDon(HoaDon newHoaDon, Long id) {
        Optional<HoaDon> foundHD = hoaDonRepository.findById(id);

        if(!foundHD.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find HoaDon with id = " + id, "")
            );
        }

        HoaDon updatedHD = foundHD
                .map(hoaDon -> {
                    hoaDon.setNgayLap(newHoaDon.getNgayLap());

                    return hoaDonRepository.save(hoaDon);
                }).orElseGet(() -> {
                    return null;
                });

        // Thông báo sự kiện "HOADON_UPDATED"
        notifyListeners("HOADON_UPDATED", updatedHD);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update HoaDon Success", updatedHD)
        );
    }

    public ResponseEntity<ResponseObject> deleteHoaDon(Long hoaDonId) {
        Optional<HoaDon> foundHD = hoaDonRepository.findById(hoaDonId);

        if(!foundHD.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can't find HoaDon with id = " + hoaDonId, "")
            );
        }

        // Find All ChiTietHoaDon relevant to Delete
        List<ChiTietHoaDon> boxCTHD = chiTietHDRepository.findAllByHoaDon(foundHD);

        // Get some info for parameter
        Long[] boxSPId = new Long[boxCTHD.size()];
        ChiTietHoaDon[] boxCTHDterm = new ChiTietHoaDon[boxCTHD.size()];
        int index = 0;
        ChiTietHoaDonCollection collection = new ChiTietHDList(boxCTHD);
        ChiTietHoaDonIterator iterator = collection.iterator();
        while (iterator.hasNext()) {
            ChiTietHoaDon item = iterator.next();
            boxSPId[index] = item.getSanPham().getId();
            boxCTHDterm[index] = new ChiTietHoaDon(null, 0, item.getSoLuong() * -1, 0, null, null, null);
            index++;
        }

        // Change quantity for QuanLySP
        ResponseEntity<ResponseObject> checkQuantity = quanLySPService.deleteQuantityQuanLySP(boxSPId, foundHD.get().getChiNhanh().getId(), boxCTHDterm);
        if(checkQuantity != null) {
            return checkQuantity;
        }

        // Delete All ChiTietHoaDon
        chiTietHDRepository.deleteAll(boxCTHD);

        // Delete
        hoaDonRepository.deleteById(hoaDonId);

        // Thông báo sự kiện "HOADON_DELETED"
        notifyListeners("HOADON_DELETED", foundHD.get());

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete HoaDon Success", "")
        );
    }

    public ResponseEntity<ResponseObject> createHoaDon(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);
        if(!chiNhanh.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Can't find ChiNhanh with id = " + chiNhanhId, "")
            );
        }

        HoaDon hoaDon = (HoaDon) phieuFactoryProvider.getFactory("hoadon").createPhieu(chiNhanh.get());
        HoaDon savedHoaDon = hoaDonRepository.save(hoaDon);

        // Thông báo sự kiện "HOADON_CREATED"
        notifyListeners("HOADON_CREATED", savedHoaDon);

        return ResponseEntity.status(HttpStatus.OK).body(
            new ResponseObject("ok", "Create HoaDon Success", savedHoaDon)
        );
    }
}
