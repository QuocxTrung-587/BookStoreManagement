package vn.tdtu.finalterm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.tdtu.finalterm.models.ChiTietHoaDon;
import vn.tdtu.finalterm.models.HoaDon;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDTO {
    @NotBlank(message = "Please input taiKhoan")
    private String taiKhoan;

    @NotBlank(message = "Please input matKhau")
    private String matKhau;

    @NotBlank(message = "Please input matKhauMoi")
    private String matKhauMoi;
}
