package vn.tdtu.finalterm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.service.command.AdminCommandInvoker;
import vn.tdtu.finalterm.service.command.chinhanh.CreateChiNhanhCommand;
import vn.tdtu.finalterm.service.command.sanpham.CreateSanPhamCommand;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminCommandInvoker commandInvoker;
    @Autowired
    private ChiNhanhRepository chiNhanhRepository;
    @Autowired
    private SanPhamRepository sanPhamRepository;

    @PostMapping("/chiNhanh")
    public ResponseEntity<ResponseObject> createChiNhanh(@RequestBody ChiNhanh chiNhanh) {
        CreateChiNhanhCommand command = new CreateChiNhanhCommand(chiNhanhRepository, chiNhanh);
        ChiNhanh result = commandInvoker.executeCommand(command);
        return ResponseEntity.ok(new ResponseObject("ok", "Create ChiNhanh success", result));
    }

    @PostMapping("/sanPham")
    public ResponseEntity<ResponseObject> createSanPham(@RequestBody SanPham sanPham) {
        CreateSanPhamCommand command = new CreateSanPhamCommand(sanPhamRepository, sanPham);
        SanPham result = commandInvoker.executeCommand(command);
        return ResponseEntity.ok(new ResponseObject("ok", "Create SanPham success", result));
    }

    @PostMapping("/undo")
    public ResponseEntity<ResponseObject> undoLastOperation() {
        commandInvoker.undoLastCommand();
        return ResponseEntity.ok(new ResponseObject("ok", "Undo operation successful", null));
    }
}