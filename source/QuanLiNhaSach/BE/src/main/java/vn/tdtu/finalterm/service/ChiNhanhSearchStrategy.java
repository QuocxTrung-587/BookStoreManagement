package vn.tdtu.finalterm.service;

import org.springframework.http.ResponseEntity;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import java.util.List;
import java.util.Optional;

// Interface định nghĩa chiến lược tìm kiếm
interface SearchStrategy {
    public ResponseEntity<ResponseObject> findAll();
}

