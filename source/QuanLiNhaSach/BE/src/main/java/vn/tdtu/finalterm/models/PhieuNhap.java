package vn.tdtu.finalterm.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.Accessors;
import vn.tdtu.finalterm.service.factory.Phieu;

import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "tbl_PhieuNhap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = "chiTietPhieuNhapList")
public class PhieuNhap implements Phieu {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull(message = "Please input NgayNhap")
    private Date ngayNhap;

    @PositiveOrZero
    private float tongCong;

    @ManyToOne
    @JoinColumn(name = "fk_chiNhanh")
    private ChiNhanh chiNhanh;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "phieuNhap")
    private List<ChiTietPhieuNhap> chiTietPhieuNhapList;

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Date getNgay() {
        return this.ngayNhap;
    }

    @Override
    public void setNgay(Date ngay) {
        this.ngayNhap = ngay;
    }

    @Override
    public float getTongCong() {
        return this.tongCong;
    }

    @Override
    public void setTongCong(float tongCong) {
        this.tongCong = tongCong;
    }

    @Override
    public ChiNhanh getChiNhanh() {
        return this.chiNhanh;
    }

    @Override
    public void setChiNhanh(ChiNhanh chiNhanh) {
        this.chiNhanh = chiNhanh;
    }
}
