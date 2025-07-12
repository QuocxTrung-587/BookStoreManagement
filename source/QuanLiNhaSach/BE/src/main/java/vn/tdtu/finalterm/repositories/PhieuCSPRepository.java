package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.PhieuChuyenSanPham;

import java.util.List;

@Repository
public interface PhieuCSPRepository extends JpaRepository<PhieuChuyenSanPham, Long> {
    List<PhieuChuyenSanPham> findAllByChiNhanhFrom(ChiNhanh chiNhanh);

    List<PhieuChuyenSanPham> findAllByChiNhanhTo(ChiNhanh chiNhanh);
}
