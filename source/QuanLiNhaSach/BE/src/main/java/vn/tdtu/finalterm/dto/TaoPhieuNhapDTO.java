package vn.tdtu.finalterm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.tdtu.finalterm.models.ChiTietPhieuNhap;
import vn.tdtu.finalterm.models.PhieuNhap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaoPhieuNhapDTO {
    private Long[] sanPhamId;
    private Long chiNhanhId;
    // input ngayNhap
    private PhieuNhap phieuNhap;
    // input GN, SL
    private ChiTietPhieuNhap[] chiTietPhieuNhap;
}
