package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ChiTietPhieuNhap;
import vn.tdtu.finalterm.models.PhieuNhap;
import vn.tdtu.finalterm.models.SanPham;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChiTietPNRepository extends JpaRepository<ChiTietPhieuNhap, Long> {
    List<ChiTietPhieuNhap> findAllByChiNhanh(Optional<ChiNhanh> chiNhanh);

    List<ChiTietPhieuNhap> findAllBySanPham(Optional<SanPham> sanPham);

    List<ChiTietPhieuNhap> findAllByChiNhanhAndSanPham(Optional<ChiNhanh> chiNhanh, Optional<SanPham> sanPham);

    List<ChiTietPhieuNhap> findAllByPhieuNhap(Optional<PhieuNhap> foundPN);
}
