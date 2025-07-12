package vn.tdtu.finalterm.service.factory;

import org.springframework.stereotype.Component;
import vn.tdtu.finalterm.models.PhieuNhap;
import vn.tdtu.finalterm.models.ChiNhanh;

import java.sql.Date;
import java.time.LocalDate;

@Component
public class PhieuNhapFactory extends PhieuFactory {
    @Override
    public Phieu createPhieu(ChiNhanh chiNhanh) {
        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setChiNhanh(chiNhanh);
        phieuNhap.setNgayNhap(Date.valueOf(LocalDate.now()));
        phieuNhap.setTongCong(0);
        return phieuNhap;
    }
}