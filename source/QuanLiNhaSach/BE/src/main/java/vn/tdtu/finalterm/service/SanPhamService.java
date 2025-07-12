package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.SanPham;
import vn.tdtu.finalterm.repositories.SanPhamRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SanPhamService {
    @Autowired
    private SanPhamRepository sanPhamRepository;

    public ResponseEntity<ResponseObject> findAllSanPham() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All SanPham Success", sanPhamRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findSanPhamById(Long sanPhamId) {
        Optional<SanPham> sanPham = sanPhamRepository.findById(sanPhamId);

        return sanPham.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query SanPham Success", sanPham)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find SanPham with id = " + sanPhamId, "")
                );
    }

    public ResponseEntity<ResponseObject> insertSanPham(SanPham sanPham) {
        List<SanPham> foundSanPham = sanPhamRepository.findByTenSanPham(sanPham.getTenSanPham().trim());
        // Trường lỡ ghi thêm field id
        Optional<SanPham> foundSanPhamWithId = (sanPham.getId() == null) ? Optional.empty() : sanPhamRepository.findById(sanPham.getId());

        if(foundSanPham.size() > 0 || foundSanPhamWithId.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "tenSanPham already taken or the same id", "")
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert SanPham Success", sanPhamRepository.save(sanPham))
        );
    }

    public ResponseEntity<ResponseObject> updateSanPham(SanPham newSanPham, Long id) {
        List<SanPham> foundSanPham = sanPhamRepository.findByTenSanPham(newSanPham.getTenSanPham().trim());
        if(foundSanPham.size() > 0) {
            // Trường hợp update nhưng tên sản phẩm trùng thì chỉ cho thay đổi field khi khác id
            if(foundSanPham.get(0).getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "tenSanPham already taken", "")
                );
            }
        }

        SanPham updatedSanPham = sanPhamRepository.findById(id)
                .map(sanPham -> {
                    sanPham.setTenSanPham(newSanPham.getTenSanPham());
                    sanPham.setLoaiSanPham(newSanPham.getLoaiSanPham());
                    sanPham.setThuongHieu(newSanPham.getThuongHieu());
                    sanPham.setTacGia(newSanPham.getTacGia());
                    sanPham.setTheLoai(newSanPham.getTheLoai());

                    return sanPhamRepository.save(sanPham);
                }).orElseGet(() -> {
                    newSanPham.setId(id);
                    return sanPhamRepository.save(newSanPham);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update SanPham Success", updatedSanPham)
        );
    }

    public ResponseEntity<ResponseObject> deleteSanPham(Long id) {
        boolean isExist = sanPhamRepository.existsById(id);

        if(isExist) {
            sanPhamRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete SanPham Success", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find SanPham to delete", "")
        );
    }

    public ResponseEntity<ResponseObject> findSPByTenOrLoaiOrTHOrTGOrTL(String key) {
        List<SanPham> foundSP = sanPhamRepository.findByTenSanPhamContainsOrLoaiSanPhamContainsOrThuongHieuContainsOrTacGiaContainsOrTheLoaiContains(key, key, key, key, key);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query SanPham Success", foundSP)
        );
    }
}
