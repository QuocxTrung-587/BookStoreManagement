package vn.tdtu.finalterm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.tdtu.finalterm.models.ChiTietHoaDon;
import vn.tdtu.finalterm.models.HoaDon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaoHoaDonDTO {
    private Long[] sanPhamId;
    private Long chiNhanhId;
    // input ngayLap
    private HoaDon hoaDon;
    // input GB, SL
    private ChiTietHoaDon[] chiTietHoaDon;
}
