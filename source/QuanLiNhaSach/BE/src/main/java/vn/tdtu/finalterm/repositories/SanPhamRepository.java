package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.SanPham;

import java.util.List;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {
    List<SanPham> findByTenSanPham(String trim);

    List<SanPham> findByTenSanPhamContainsOrLoaiSanPhamContainsOrThuongHieuContainsOrTacGiaContainsOrTheLoaiContains(String tenSanPham, String loaiSanPham, String thuongHieu, String tacGia, String theLoai);
}
