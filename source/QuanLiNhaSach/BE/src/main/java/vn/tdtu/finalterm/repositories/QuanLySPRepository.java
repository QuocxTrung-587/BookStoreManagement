package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.QuanLySanPham;
import vn.tdtu.finalterm.models.SanPham;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuanLySPRepository extends JpaRepository<QuanLySanPham, Long> {
    List<QuanLySanPham> findAllByMaCN(ChiNhanh chiNhanh);

    List<QuanLySanPham> findAllByMaSP(SanPham sanPham);

    Optional<QuanLySanPham> findByMaSPAndMaCN(SanPham sanPham, ChiNhanh chiNhanh);

    Optional<QuanLySanPham> findByMaSPIdAndMaCNId(Long id, Long chiNhanhId);

    List<QuanLySanPham> findAllByMaCNIn(List<ChiNhanh> chiNhanh);

    List<QuanLySanPham> findAllByTrangThai(int i);

    List<QuanLySanPham> findAllByMaCNAndMaSP(Optional<ChiNhanh> chiNhanh, Optional<SanPham> sanPham);
}
