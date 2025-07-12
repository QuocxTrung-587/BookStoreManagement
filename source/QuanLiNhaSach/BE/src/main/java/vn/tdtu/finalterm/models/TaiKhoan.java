package vn.tdtu.finalterm.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;
import vn.tdtu.finalterm.service.states.AccountState;
import vn.tdtu.finalterm.service.states.UnverifiedState;

import java.util.List;

@Entity
@Table(name = "tbl_TaiKhoan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
@ToString(exclude = "chiNhanh")
public class TaiKhoan {
    @Id
    @NotBlank(message = "Please input taiKhoan")
    private String taiKhoan;

    @NotBlank(message = "Please input matKhau")
    private String matKhau;

    private boolean enabled = false;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "taiKhoanFK")
    @JsonIgnore
    private ChiNhanh chiNhanh;

    @Transient
    private AccountState state;

    public TaiKhoan(String taiKhoan, String matKhau, boolean enabled, ChiNhanh chiNhanh) {
        this.taiKhoan = taiKhoan;
        this.matKhau = matKhau;
        this.enabled = enabled;
        this.chiNhanh = chiNhanh;
        this.state = new UnverifiedState(); // Set default state
    }

    public void setState(AccountState state) {
        this.state = state;
        state.handleState(this);
    }
    
    public String getStateName() {
        return state != null ? state.getStateName() : "UNKNOWN";
    }
}
