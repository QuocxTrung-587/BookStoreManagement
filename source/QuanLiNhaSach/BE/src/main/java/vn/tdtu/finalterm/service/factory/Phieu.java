package vn.tdtu.finalterm.service.factory;

import vn.tdtu.finalterm.models.ChiNhanh;

import java.sql.Date;

public interface Phieu {
    Long getId();
    void setId(Long id);
    Date getNgay();
    void setNgay(Date ngay);
    float getTongCong();
    void setTongCong(float tongCong);
    ChiNhanh getChiNhanh();
    void setChiNhanh(ChiNhanh chiNhanh);
}