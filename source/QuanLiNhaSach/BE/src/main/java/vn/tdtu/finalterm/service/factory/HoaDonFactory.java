package vn.tdtu.finalterm.service.factory;

import org.springframework.stereotype.Component;
import vn.tdtu.finalterm.models.HoaDon;
import vn.tdtu.finalterm.models.ChiNhanh;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class HoaDonFactory extends PhieuFactory {
    @Override
    public Phieu createPhieu(ChiNhanh chiNhanh) {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setChiNhanh(chiNhanh);
        hoaDon.setNgayLap(Date.valueOf(LocalDate.now()));
        hoaDon.setTongCong(0);
        return hoaDon;
    }
}