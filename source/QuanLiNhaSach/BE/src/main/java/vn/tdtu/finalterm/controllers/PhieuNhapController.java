package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.PhieuNhap;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.service.PhieuNhapService;

@RestController
@RequestMapping(path = "")
public class PhieuNhapController {
    @Autowired
    PhieuNhapService phieuNhapService;

    @GetMapping("/phieuNhap")
    public ResponseEntity<ResponseObject> findAllPhieuNhap() {
        return phieuNhapService.findAllPhieuNhap();
    }

    @GetMapping("/phieuNhap/{chiNhanhId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllPNByChiNhanhId(@PathVariable("chiNhanhId") Long chiNhanhId) {
        return phieuNhapService.findAllPNByChiNhanhId(chiNhanhId);
    }

    @PostMapping("/phieuNhapQuery") // Custom Router
    public ResponseEntity<ResponseObject> findAllPNByNgayNhap(@RequestBody PhieuNhap phieuNhap) {
        return phieuNhapService.findAllPNByNgayNhap(phieuNhap);
    }

    @PutMapping("/phieuNhap/{id}")
    public ResponseEntity<ResponseObject> updatePhieuNhap(@Valid @RequestBody PhieuNhap phieuNhap, @PathVariable("id") Long id) {
        return phieuNhapService.updatePhieuNhap(phieuNhap, id);
    }

    @DeleteMapping("/phieuNhap/{phieuNhapId}")
    public ResponseEntity<ResponseObject> deletePhieuNhap(@PathVariable("phieuNhapId") Long phieuNhapId) {
        return phieuNhapService.deletePhieuNhap(phieuNhapId);
    }
}
