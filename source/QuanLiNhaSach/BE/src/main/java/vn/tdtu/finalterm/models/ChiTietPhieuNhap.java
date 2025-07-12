package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "tbl_ChiTietPhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ChiTietPhieuNhap {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @PositiveOrZero
    @NotNull(message = "Please input giaNhap")
    private float giaNhap;

    @PositiveOrZero
    @NotNull(message = "Please input soLuong")
    private int soLuong;

    @PositiveOrZero
    private float tongTien;

    @ManyToOne
    @JoinColumn(name = "fk_sanPham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanh")
    private ChiNhanh chiNhanh;

    @ManyToOne
    @JoinColumn(name = "fk_phieuNhap")
    @JsonIgnoreProperties("chiTietPhieuNhapList")
    private PhieuNhap phieuNhap;

    @PrePersist
    @PreUpdate
    private void calculateTongTien() {
        this.tongTien = this.soLuong * this.giaNhap;
    }
}
