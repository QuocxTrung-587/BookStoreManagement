package vn.tdtu.finalterm.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.service.ChiNhanhService;

import java.util.List;

@RestController
@RequestMapping(path = "")
public class ChiNhanhController {
    @Autowired
    ChiNhanhService chiNhanhService;

    @GetMapping("/chiNhanh")
    public ResponseEntity<ResponseObject> findAllChiNhanh() {
        return chiNhanhService.findAll();
    }

    @GetMapping("/chiNhanh/{id}")
    public ResponseEntity<ResponseObject> findChiNhanhById(@PathVariable("id") Long id) {
        return chiNhanhService.findChiNhanhById(id);
    }

    @GetMapping("/chiNhanhQuery/{key}") // Find Request
    public ResponseEntity<ResponseObject> findCNByTenOrDiaChi(@PathVariable("key") String key) {
        return chiNhanhService.findCNByTenOrDiaChi(key);
    }

    @GetMapping("/chiNhanhQuery2/{key}") // Find Request
    public ResponseEntity<ResponseObject> findCNByTen(@PathVariable("key") String key) {
        return chiNhanhService.findCNByTen(key);
    }

    @PostMapping("/chiNhanh")
    public ResponseEntity<ResponseObject> insertChiNhanh(@Valid @RequestBody ChiNhanh chiNhanh, final HttpServletRequest request) {
        return chiNhanhService.insertChiNhanh(chiNhanh, request);
    }

    @PostMapping("/chiNhanhByTK") // Custom Router
    public ResponseEntity<ResponseObject> findChiNhanhByTaiKhoan(@Valid @RequestBody TaiKhoan taiKhoan) {
        return chiNhanhService.findChiNhanhByTaiKhoan(taiKhoan);
    }

    @PutMapping("/chiNhanh/{id}")
    public ResponseEntity<ResponseObject> updateChiNhanh(@RequestBody ChiNhanh chiNhanh, @PathVariable("id") Long id) {
        return chiNhanhService.updateChiNhanh(chiNhanh, id);
    }

    @DeleteMapping("/chiNhanh/{id}")
    public ResponseEntity<ResponseObject> deleteChiNhanh(@PathVariable("id") Long id) {
        return chiNhanhService.deleteChiNhanh(id);
    }
}
