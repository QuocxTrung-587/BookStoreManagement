package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

@Entity
@Table(name = "tbl_SanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = {"quanLySanPhamList", "chiTietPhieuNhapList", "chiTietHoaDonList"})
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please input tenSanPham")
    private String tenSanPham;

    @NotBlank(message = "Please input loaiSanPham")
    private String loaiSanPham;

    @NotBlank(message = "Please input thuongHieu")
    private String thuongHieu;

    private String tacGia;

    private String theLoai;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "maSP")
    @JsonIgnore
    private List<QuanLySanPham> quanLySanPhamList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sanPham")
    @JsonIgnore
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sanPham")
    @JsonIgnore
    private List<ChiTietHoaDon> chiTietHoaDonList;
}
