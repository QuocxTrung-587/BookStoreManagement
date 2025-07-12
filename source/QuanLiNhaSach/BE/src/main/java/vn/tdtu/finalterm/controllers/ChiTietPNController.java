package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.dto.TaoPhieuNhapDTO;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.service.ChiTietPNService;

@RestController
@RequestMapping(path = "")
public class ChiTietPNController {
    @Autowired
    ChiTietPNService chiTietPNService;

    @GetMapping("/chiTietPN")
    public ResponseEntity<ResponseObject> findAllChiTietPN() {
        return chiTietPNService.findAllChiTietPN();
    }

    @GetMapping("/chiTietPN/{id}") // Custom Router
    public ResponseEntity<ResponseObject> findAllChiTietPNByChiNhanhId(@PathVariable("id") Long chiNhanhId) {
        return chiTietPNService.findAllChiTietPNByChiNhanhId(chiNhanhId);
    }

    @GetMapping("/chiTietPNBySanPham/{id}") // Custom Router
    public ResponseEntity<ResponseObject> findAllChiTietPNBySanPhamId(@PathVariable("id") Long sanPhamId) {
        return chiTietPNService.findAllChiTietPNBySanPhamId(sanPhamId);
    }

    @GetMapping("/chiTietPN/{chiNhanhId}/{sanPhamId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllChiTietPNByCNIdAndSPId(@PathVariable("chiNhanhId") Long chiNhanhId, @PathVariable("sanPhamId") Long sanPhamId) {
        return chiTietPNService.findAllChiTietPNByCNIdAndSPId(chiNhanhId, sanPhamId);
    }

    @PostMapping("/chiTietPN") // Custom Router
    public ResponseEntity<ResponseObject> insertChiTietPNAndPNAndQLSP(@Valid @RequestBody TaoPhieuNhapDTO taoPhieuNhapDTO) {
        return chiTietPNService.insertChiTietPNAndPNAndQLSP(taoPhieuNhapDTO);
    }

    @PutMapping("/chiTietPN/{id}")
    public ResponseEntity<ResponseObject> updateChiTietPN(@Valid @RequestBody ChiTietPhieuNhap chiTietPhieuNhap, @PathVariable("id") Long chiTietPNId) {
        return chiTietPNService.updateChiTietPN(chiTietPhieuNhap, chiTietPNId);
    }

    @DeleteMapping("/chiTietPN/{id}")
    public ResponseEntity<ResponseObject> deleteChiTietPN(@PathVariable("id") Long chiTietPNId) {
        return chiTietPNService.deleteChiTietPN(chiTietPNId);
    }
}
