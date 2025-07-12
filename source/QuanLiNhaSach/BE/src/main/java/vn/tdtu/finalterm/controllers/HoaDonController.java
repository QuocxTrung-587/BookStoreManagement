package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.HoaDon;
import vn.tdtu.finalterm.models.PhieuNhap;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.service.HoaDonService;
import vn.tdtu.finalterm.facade.OrderFacade;
import vn.tdtu.finalterm.dto.TaoHoaDonDTO;

@RestController
@RequestMapping(path = "")
public class HoaDonController {
    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    HoaDonService hoaDonService;

    @GetMapping("/hoaDon")
    public ResponseEntity<ResponseObject> findAllHoaDon() {
        return hoaDonService.findAllHoaDon();
    }

    @GetMapping("/hoaDon/{chiNhanhId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllHDByChiNhanhId(@PathVariable("chiNhanhId") Long chiNhanhId) {
        return hoaDonService.findAllHDByChiNhanhId(chiNhanhId);
    }

    @GetMapping("/hoaDonQuery/{key}") // Find Request
    public ResponseEntity<ResponseObject> findHDByTenChiNhanh(@PathVariable("key") String key) {
        return hoaDonService.findHDByTenChiNhanh(key);
    }

    @PostMapping("/hoaDonQuery") // Custom Router
    public ResponseEntity<ResponseObject> findAllHDByNgayLap(@RequestBody HoaDon hoaDon) {
        return hoaDonService.findHDByNgayLap(hoaDon);
    }

    @PutMapping("/hoaDon/{id}")
    public ResponseEntity<ResponseObject> updateHoaDon(@Valid @RequestBody HoaDon hoaDon, @PathVariable("id") Long id) {
        return hoaDonService.updateHoaDon(hoaDon, id);
    }

    @DeleteMapping("/hoaDon/{hoaDonId}")
    public ResponseEntity<ResponseObject> deleteHoaDon(@PathVariable("hoaDonId") Long hoaDonId) {
        return hoaDonService.deleteHoaDon(hoaDonId);
    }

    @PostMapping("/order")
    public ResponseEntity<ResponseObject> createOrder(@RequestBody TaoHoaDonDTO orderDTO) {
        return orderFacade.createOrder(orderDTO);
    }

    @DeleteMapping("/order/{orderId}")
    public ResponseEntity<ResponseObject> cancelOrder(@PathVariable Long orderId) {
        return orderFacade.cancelOrder(orderId);
    }
}
