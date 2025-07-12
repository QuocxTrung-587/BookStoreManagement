package vn.tdtu.finalterm.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.dto.ChangePasswordDTO;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.service.TaiKhoanService;

@RestController
@RequestMapping(path = "")
public class TaiKhoanController {
    @Autowired
    TaiKhoanService taiKhoanService;

    @PostMapping("/taiKhoan") // Custom Router
    public ResponseEntity<ResponseObject> dangNhap(@Valid @RequestBody TaiKhoan taiKhoan) {
        return taiKhoanService.dangNhap(taiKhoan);
    }

    @PutMapping("/taiKhoan") // Custom Router
    public ResponseEntity<ResponseObject> doiMatKhau(@RequestBody TaiKhoan taiKhoan, final HttpServletRequest request) {
        return taiKhoanService.doiMatKhau(taiKhoan, request);
    }

    @PutMapping("/taiKhoanChange") // Custom Router
    public ResponseEntity<ResponseObject> doiMatKhauTheoNguoiDung(@Valid @RequestBody ChangePasswordDTO taiKhoan) {
        return taiKhoanService.doiMatKhauTheoNguoiDung(taiKhoan);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<ResponseObject> verifyRegistration(@RequestParam("token") String token) {
        return taiKhoanService.verifyRegistration(token);
    }

    @GetMapping("/resendToken")
    public ResponseEntity<ResponseObject> resendToken(final HttpServletRequest request) {
        return taiKhoanService.resendVerificationToken(request);
    }
}
