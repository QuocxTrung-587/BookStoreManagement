package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "tbl_ChiTietHoaDon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class ChiTietHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float giaBan;
    private int soLuong;
    private float tongTien;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanh")
    private ChiNhanh chiNhanh;

    @ManyToOne
    @JoinColumn(name = "fk_sanPham")
    private SanPham sanPham;

    @ManyToOne
    @JoinColumn(name = "fk_hoaDon")
    @JsonIgnoreProperties("chiTietHoaDonList")
    private HoaDon hoaDon;

    @PrePersist
    @PreUpdate
    private void calculateTongTien() {
        this.tongTien = this.soLuong * this.giaBan;
    }
}
