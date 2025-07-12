package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ChiTietHoaDon;
import vn.tdtu.finalterm.models.HoaDon;
import vn.tdtu.finalterm.models.SanPham;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietHDRepository extends JpaRepository<ChiTietHoaDon, Long> {
    List<ChiTietHoaDon> findAllByChiNhanh(Optional<ChiNhanh> chiNhanh);

    List<ChiTietHoaDon> findAllByChiNhanhAndSanPham(Optional<ChiNhanh> chiNhanh, Optional<SanPham> sanPham);

    List<ChiTietHoaDon> findAllByHoaDon(Optional<HoaDon> foundHD);
}
