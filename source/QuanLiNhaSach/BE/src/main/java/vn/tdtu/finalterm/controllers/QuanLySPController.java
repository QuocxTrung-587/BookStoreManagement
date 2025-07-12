package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.QuanLySanPham;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.service.QuanLySPService;

@RestController
@RequestMapping(path = "")
public class QuanLySPController {
    @Autowired
    QuanLySPService quanLySPService;

    @GetMapping("/quanLySP")
    public ResponseEntity<ResponseObject> findAllQuanLySP() {
        return quanLySPService.findAllQuanLySP();
    }

    @GetMapping("/quanLySPByCN") // Custom Router
    public ResponseEntity<ResponseObject> findAllQuanLySPByChiNhanh(@Valid @RequestBody ChiNhanh chiNhanh) {
        return quanLySPService.findAllQuanLySPByChiNhanh(chiNhanh);
    }

    @GetMapping("/quanLySPByCN/{id}") // Custom Router
    public ResponseEntity<ResponseObject> findAllQuanLySPByChiNhanhId(@PathVariable("id") Long chiNhanhId) {
        return quanLySPService.findAllQuanLySPByChiNhanhId(chiNhanhId);
    }

    @GetMapping("/quanLySPBySP/{id}") // Custom Router
    public ResponseEntity<ResponseObject> findAllQuanLySPBySanPhamId(@PathVariable("id") Long sanPhamId) {
        return quanLySPService.findAllQuanLySPBySanPhamId(sanPhamId);
    }

    @GetMapping("/quanLySP/{chiNhanhId}/{sanPhamId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllQLSPByCNIdAndSPId(@PathVariable("chiNhanhId") Long chiNhanhId, @PathVariable("sanPhamId") Long sanPhamId) {
        return quanLySPService.findAllQLSPByCNIdAndSPId(chiNhanhId, sanPhamId);
    }

    @GetMapping("/quanLySPQuery/{key}") // Find Request
    public ResponseEntity<ResponseObject> findQuanLySPByTenCN(@PathVariable("key") String key) {
        return quanLySPService.findQuanLySPByTenCN(key);
    }

    @GetMapping("/quanLySPQuery/expired") // Find Request
    public ResponseEntity<ResponseObject> findQLSPThatExpired() {
        return quanLySPService.findQLSPThatExpired();
    }

    @PutMapping("/quanLySP/{id}")
    public ResponseEntity<ResponseObject> updateQuanLySP(@RequestBody QuanLySanPham quanLySanPham, @PathVariable("id") Long quanLySPId) {
        return quanLySPService.updateQuanLySP(quanLySanPham, quanLySPId);
    }

    @PutMapping("/quanLySP/moveSP/{id}") // Custom Router
    public ResponseEntity<ResponseObject> moveSPInSameCN(@RequestBody QuanLySanPham quanLySanPham, @PathVariable("id") Long quanLySPId) {
        return quanLySPService.moveSPInSameCN(quanLySanPham, quanLySPId);
    }

    @PutMapping("/quanLySP/updateTrangThai") // Custom Router
    public ResponseEntity<ResponseObject> updateAllTrangThai() {
        return quanLySPService.updateAllTrangThai();
    }
}
