package vn.tdtu.finalterm.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.dto.TaoHoaDonDTO;
import vn.tdtu.finalterm.models.ChiTietHoaDon;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.service.ChiTietHDService;

@RestController
@RequestMapping(path = "")
public class ChiTietHDController {
    @Autowired
    ChiTietHDService chiTietHDService;

    @GetMapping("/chiTietHD")
    public ResponseEntity<ResponseObject> findAllChiTietHD() {
        return chiTietHDService.findAll();
    }

    @GetMapping("/chiTietHD/{id}") // Custom Router
    public ResponseEntity<ResponseObject> findAllChiTietHDByChiNhanhId(@PathVariable("id") Long chiNhanhId) {
        return chiTietHDService.findAllChiTietHDByChiNhanhId(chiNhanhId);
    }

    @GetMapping("/chiTietHD/{chiNhanhId}/{sanPhamId}") // Custom Router
    public ResponseEntity<ResponseObject> findAllChiTietHDByCNIdAndSPId(@PathVariable("chiNhanhId") Long chiNhanhId, @PathVariable("sanPhamId") Long sanPhamId) {
        return chiTietHDService.findAllChiTietHDByCNIdAndSPId(chiNhanhId, sanPhamId);
    }

    @PostMapping("/chiTietHD") // Custom Router
    public ResponseEntity<ResponseObject> insertChiTietHDAndHD(@Valid @RequestBody TaoHoaDonDTO taoHoaDonDTO) {
        return chiTietHDService.insertChiTietHDAndHD(taoHoaDonDTO);
    }

    @PutMapping("/chiTietHD/{id}")
    public ResponseEntity<ResponseObject> updateChiTietHD(@Valid @RequestBody ChiTietHoaDon chiTietHoaDon, @PathVariable("id") Long chiTietHDId) {
        return chiTietHDService.updateChiTietHD(chiTietHoaDon, chiTietHDId);
    }

    @DeleteMapping("/chiTietHD/{id}")
    public ResponseEntity<ResponseObject> deleteChiTietHD(@PathVariable("id") Long chiTietHDId) {
        return chiTietHDService.deleteChiTietHD(chiTietHDId);
    }
}
