package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.PhieuChuyenSanPham;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.service.PhieuCSPService;

@RestController
@RequestMapping(path = "")
public class PhieuCSPController {
    @Autowired
    PhieuCSPService phieuCSPService;

    @GetMapping("/phieuCSP")
    public ResponseEntity<ResponseObject> findAllPhieuCSP() {
        return phieuCSPService.findAllPhieuCSP();
    }

    @GetMapping("/phieuCSPFrom/{chiNhanhId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllPhieuCSPByChiNhanhFromId(@PathVariable("chiNhanhId") Long chiNhanhId) {
        return phieuCSPService.findAllPhieuCSPByChiNhanhFromId(chiNhanhId);
    }

    @GetMapping("/phieuCSPTo/{chiNhanhId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllPhieuCSPByChiNhanhToId(@PathVariable("chiNhanhId") Long chiNhanhId) {
        return phieuCSPService.findAllPhieuCSPByChiNhanhToId(chiNhanhId);
    }

    @PostMapping("/phieuCSP")
    public ResponseEntity<ResponseObject> insertPhieuCSP(@Valid @RequestBody PhieuChuyenSanPham phieuChuyenSanPham) {
        return phieuCSPService.insertPhieuCSP(phieuChuyenSanPham);
    }
}
