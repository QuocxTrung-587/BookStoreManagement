package vn.tdtu.finalterm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.models.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
    VerificationToken findByTaiKhoan(TaiKhoan taiKhoan);

    void deleteByTaiKhoan(TaiKhoan taiKhoan);
}
