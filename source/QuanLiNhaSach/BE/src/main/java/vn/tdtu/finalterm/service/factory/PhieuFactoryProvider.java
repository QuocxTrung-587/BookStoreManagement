package vn.tdtu.finalterm.service.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhieuFactoryProvider {
    @Autowired
    private PhieuNhapFactory phieuNhapFactory;
    
    @Autowired
    private HoaDonFactory hoaDonFactory;

    public PhieuFactory getFactory(String type) {
        switch (type.toLowerCase()) {
            case "phieunhap":
                return phieuNhapFactory;
            case "hoadon":
                return hoaDonFactory;
            default:
                throw new IllegalArgumentException("Unknown phieu type: " + type);
        }
    }
}