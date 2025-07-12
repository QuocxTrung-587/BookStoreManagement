package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.HoaDon;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {
    List<HoaDon> findAllByChiNhanh(Optional<ChiNhanh> chiNhanh);

    List<HoaDon> findAllByChiNhanhIn(List<ChiNhanh> chiNhanhList);

    List<HoaDon> findByNgayLap(Date ngayLap);
}
