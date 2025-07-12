package vn.tdtu.finalterm.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.Accessors;

import java.sql.Date;

@Entity
@Table(name = "tbl_PhieuChuyenSanPham")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class PhieuChuyenSanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Please input ngayChuyen")
    private Date ngayChuyen;

    @PositiveOrZero
    private int soLuongChuyen;

    @ManyToOne
    @JoinColumn(name = "fk_QLSPFrom")
    private QuanLySanPham quanLySanPham;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanhFrom")
    private ChiNhanh chiNhanhFrom;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanhTo")
    private ChiNhanh chiNhanhTo;
}
