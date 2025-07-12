package vn.tdtu.finalterm.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.tdtu.finalterm.enums.DefaultValues;

import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "tbl_VerificationToken")
@Data
@NoArgsConstructor
public class VerificationToken {
    //Expiration time (Second)
    private static  final int EXPIRATION_TIME = (int) DefaultValues.EXPIRATION_TIME.getValue();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_taiKhoan",
            nullable = false,
            // Foreign key constraint
            foreignKey = @ForeignKey(name = "FK_TAIKHOAN_VERIFY_TOKEN"))
    private TaiKhoan taiKhoan;

    public VerificationToken(TaiKhoan taiKhoan, String token) {
        this.token = token;
        this.taiKhoan = taiKhoan;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.SECOND, expirationTime);
        return new Date(calendar.getTime().getTime());
    }
}
