package vn.tdtu.finalterm.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Date;

@Entity
@Table(name = "tbl_QuanLySanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class QuanLySanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Please input HanTon")
    private Date hanTon;

    @PositiveOrZero
    private float giaBan;

    @PositiveOrZero
    private int trenKe;

    @PositiveOrZero
    private int trongKho;

    @PositiveOrZero
    private int tongHang;

    @PositiveOrZero
    private int trangThai;

    @ManyToOne
    @JoinColumn(name = "fk_maSP")
    private SanPham maSP;

    @ManyToOne
    @JoinColumn(name = "fk_maCN")
    private ChiNhanh maCN;

    @PrePersist
    @PreUpdate
    private void calculateTongHang() {
        this.tongHang = this.trenKe + this.trongKho;
    }
}
