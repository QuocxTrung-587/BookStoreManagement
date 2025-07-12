package vn.tdtu.finalterm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tdtu.finalterm.service.command.AdminCommandInvoker;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.repositories.SanPhamRepository;
import vn.tdtu.finalterm.service.SanPhamService;

import jakarta.validation.Valid;
import vn.tdtu.finalterm.service.command.sanpham.CreateSanPhamCommand;
import vn.tdtu.finalterm.service.command.sanpham.DeleteSanPhamCommand;
import vn.tdtu.finalterm.service.command.sanpham.UpdateSanPhamCommand;

@RestController
@RequestMapping(path = "")
public class SanPhamController {
    @Autowired
    private SanPhamService sanPhamService;
    
    @Autowired
    private SanPhamRepository sanPhamRepository;
    
    @Autowired
    private AdminCommandInvoker commandInvoker;

    @GetMapping("/sanPham")
    public ResponseEntity<ResponseObject> findAllSanPham() {
        return sanPhamService.findAllSanPham();
    }

    @GetMapping("/sanPham/{id}")
    public ResponseEntity<ResponseObject> findSanPhamById(@PathVariable("id") Long id) {
        return sanPhamService.findSanPhamById(id);
    }

    @GetMapping("/sanPhamQuery/{key}")
    public ResponseEntity<ResponseObject> findSPByTenOrLoaiOrTHOrTGOrTL(@PathVariable("key") String key) {
        return sanPhamService.findSPByTenOrLoaiOrTHOrTGOrTL(key);
    }

    @PostMapping("/sanPham")
    public ResponseEntity<ResponseObject> insertSanPham(@Valid @RequestBody SanPham sanPham) {
        CreateSanPhamCommand command = new CreateSanPhamCommand(sanPhamRepository, sanPham);
        SanPham result = commandInvoker.executeCommand(command);
        return ResponseEntity.ok(new ResponseObject("ok", "Create SanPham success", result));
    }

    @PutMapping("/sanPham/{id}")
    public ResponseEntity<ResponseObject> updateSanPham(@Valid @RequestBody SanPham sanPham, @PathVariable("id") Long id) {
        UpdateSanPhamCommand command = new UpdateSanPhamCommand(sanPhamRepository, id, sanPham);
        SanPham result = commandInvoker.executeCommand(command);
        return ResponseEntity.ok(new ResponseObject("ok", "Update SanPham success", result));
    }

    @DeleteMapping("/sanPham/{id}")
    public ResponseEntity<ResponseObject> deleteSanPham(@PathVariable("id") Long id) {
        DeleteSanPhamCommand command = new DeleteSanPhamCommand(sanPhamRepository, id);
        commandInvoker.executeCommand(command);
        return ResponseEntity.ok(new ResponseObject("ok", "Delete SanPham success", null));
    }

    @PostMapping("/sanPham/undo")
    public ResponseEntity<ResponseObject> undoLastOperation() {
        commandInvoker.undoLastCommand();
        return ResponseEntity.ok(new ResponseObject("ok", "Undo operation successful", null));
    }
}
