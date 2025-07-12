package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "tbl_ChiNhanh")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = {"quanLySanPhamList","phieuNhapList","chiTietPhieuNhapList, hoaDonList", "chiTietHoaDonList"})
public class ChiNhanh {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please input tenChiNhanh")
    private String tenChiNhanh;

    @NotBlank(message = "Please input diaChi")
    private String diaChi;

    @NotBlank(message = "Please input email")
    private String email;

    @OneToOne
    @JoinColumn(name = "fk_taiKhoan")
    private TaiKhoan taiKhoanFK;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maCN")
    @JsonIgnore
    private List<QuanLySanPham> quanLySanPhamList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chiNhanh")
    @JsonIgnore
    private List<PhieuNhap> phieuNhapList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chiNhanh")
    @JsonIgnore
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chiNhanh")
    @JsonIgnore
    private List<HoaDon> hoaDonList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chiNhanh")
    @JsonIgnore
    private List<ChiTietHoaDon> chiTietHoaDonList;
}
