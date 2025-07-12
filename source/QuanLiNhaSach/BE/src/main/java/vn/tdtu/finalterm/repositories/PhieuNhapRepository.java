package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.PhieuNhap;
import vn.tdtu.finalterm.models.SanPham;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Long> {
    List<PhieuNhap> findAllByChiNhanh(Optional<ChiNhanh> chiNhanh);

    List<PhieuNhap> findByNgayNhap(Date ngayNhap);
}
