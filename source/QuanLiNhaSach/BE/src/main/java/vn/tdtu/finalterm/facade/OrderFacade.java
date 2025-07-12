package vn.tdtu.finalterm.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.tdtu.finalterm.dto.TaoHoaDonDTO;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.HoaDon;
import vn.tdtu.finalterm.service.ChiTietHDService;
import vn.tdtu.finalterm.service.HoaDonService;
import vn.tdtu.finalterm.service.QuanLySPService;

@Component
public class OrderFacade {
    @Autowired
    private HoaDonService hoaDonService;
    @Autowired
    private ChiTietHDService chiTietHDService;
    @Autowired
    private QuanLySPService quanLySPService;

    public ResponseEntity<ResponseObject> createOrder(TaoHoaDonDTO orderDTO) {
        ResponseEntity<ResponseObject> stockResponse =
            quanLySPService.checkStockAvailability(
                orderDTO.getSanPhamId(), 
                orderDTO.getChiNhanhId(), 
                orderDTO.getChiTietHoaDon()
            );
        if (stockResponse != null) {
            return stockResponse;
        }
        ResponseEntity<ResponseObject> hoaDonResponse =
            hoaDonService.createHoaDon(orderDTO.getChiNhanhId());
        
        if (hoaDonResponse.getStatusCode().isError()) {
            return hoaDonResponse;
        }
        return chiTietHDService.insertChiTietHDAndHD(orderDTO);
    }
    public ResponseEntity<ResponseObject> cancelOrder(Long orderId) {
        return hoaDonService.deleteHoaDon(orderId);
    }
}