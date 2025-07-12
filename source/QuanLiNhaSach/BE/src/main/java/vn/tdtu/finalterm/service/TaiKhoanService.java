package vn.tdtu.finalterm.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import vn.tdtu.finalterm.dto.ChangePasswordDTO;
import vn.tdtu.finalterm.event.AccountSessionScope;
import vn.tdtu.finalterm.event.ChangePasswordCompleteEvent;
import vn.tdtu.finalterm.event.RegistrationCompleteEvent;
import vn.tdtu.finalterm.event.ResendCompleteEvent;
import vn.tdtu.finalterm.models.*;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;
import vn.tdtu.finalterm.repositories.VerificationTokenRepository;
import vn.tdtu.finalterm.service.states.UnverifiedState;
import vn.tdtu.finalterm.service.states.VerifiedState;
import vn.tdtu.finalterm.utility.JWTUtility;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class TaiKhoanService {
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private AccountSessionScope accountSessionScope;

    public ResponseEntity<ResponseObject> dangNhap(TaiKhoan taiKhoan) {
        // Check Regex Email
        if(!Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$").matcher(taiKhoan.getTaiKhoan()).matches()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Please enter the correct email structure", "")
            );
        }

        Optional<TaiKhoan> foundTK = taiKhoanRepository.findById(taiKhoan.getTaiKhoan());
        if(!foundTK.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Account not found!", "")
            );
        }

        // Check enable
        if(!foundTK.get().isEnabled()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Please verify before logging in!", "")
            );
        }

        // Generate token
        String token = null;

        if(foundTK.isPresent() && passwordEncoder.matches(taiKhoan.getMatKhau(),foundTK.get().getMatKhau())) {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                taiKhoan.getTaiKhoan(),
                                taiKhoan.getMatKhau()
                        )
                );
            } catch (BadCredentialsException e) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "INVALID_CREDENTIALS", e)
                );
            }

            // Load TaiKhoan and take UserDetails
            final UserDetails userDetails = userService.loadUserByUsername(taiKhoan.getTaiKhoan());
            // Create token
            token = jwtUtility.generateToken(userDetails);
        }

        return token != null ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Successful to login", token)
                ) :
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "Wrong Password or Account not found", "")
                );
    }

    public ResponseEntity<ResponseObject> doiMatKhau(TaiKhoan taiKhoan, HttpServletRequest request) {
        Optional<TaiKhoan> foundTaiKhoan = taiKhoanRepository.findById(taiKhoan.getTaiKhoan());

        if(!foundTaiKhoan.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Wrong Password or Account not found", "")
        );

        // Generate new password
        String passwordPrimitive = Integer.toString(new Random().nextInt(90000) + 10000);

        TaiKhoan updatedTaiKhoan = foundTaiKhoan.map(account -> {
            account.setMatKhau(passwordEncoder.encode(passwordPrimitive));

            return taiKhoanRepository.save(account);
        }).orElseGet(() -> {
            return null;
        });

        // Asynchronous activation (Send mail)
        publisher.publishEvent(new ChangePasswordCompleteEvent(
                updatedTaiKhoan,
                passwordPrimitive,
                "http://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + request.getContextPath()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change Password Success", updatedTaiKhoan)
        );
    }

    public ResponseEntity<ResponseObject> doiMatKhauTheoNguoiDung(ChangePasswordDTO taiKhoan) {
        Optional<TaiKhoan> foundTaiKhoan = taiKhoanRepository.findById(taiKhoan.getTaiKhoan());

        if(!foundTaiKhoan.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Wrong Password or Account not found", "")
        );

        if(!passwordEncoder.matches(taiKhoan.getMatKhau(),foundTaiKhoan.get().getMatKhau())) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Password not matches", "")
            );
        }

        TaiKhoan updatedTaiKhoan = foundTaiKhoan.map(account -> {
            account.setMatKhau(passwordEncoder.encode(taiKhoan.getMatKhauMoi()));

            return taiKhoanRepository.save(account);
        }).orElseGet(() -> {
            return null;
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Change Password Success", updatedTaiKhoan)
        );
    }

    public ResponseEntity<ResponseObject> verifyRegistration(String token) {
        String result = validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid")) {
            TaiKhoan taiKhoan = verificationTokenRepository.findByToken(token).getTaiKhoan();
            taiKhoan.setState(new VerifiedState());
            taiKhoanRepository.save(taiKhoan);
            
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "TaiKhoan Verified Successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Can't find token or token expired", "")
        );
    }

    public String validateVerificationToken(String token) {
        VerificationToken verificationToken
                = verificationTokenRepository.findByToken(token);

        if (verificationToken == null) {
            return "invalid";
        }

        TaiKhoan taiKhoan = verificationToken.getTaiKhoan();

        // Check time
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }

        taiKhoan.setEnabled(true);
        taiKhoanRepository.save(taiKhoan);
        return "valid";
    }

    public ResponseEntity<ResponseObject> resendVerificationToken(HttpServletRequest request) {
        if(accountSessionScope.getTaiKhoan() == null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "No accounts currently logged in!", "")
            );
        }

        // Asynchronous activation (Send mail)
        publisher.publishEvent(new ResendCompleteEvent(
                accountSessionScope.getTaiKhoan(),
                accountSessionScope.getTaiKhoan().getMatKhau(),
                "http://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + request.getContextPath()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "New tokens have been sent!", "")
        );
    }

    // Repository of VerificationToken
    // Save Token
    public void saveVerificationTokenForTK(String token, TaiKhoan taiKhoan) {
        VerificationToken verificationToken
                = new VerificationToken(taiKhoan, token);

        verificationTokenRepository.save(verificationToken);
    }

    // Replace Old Token (For Resend Email)
    public void replaceOldVerificationTokenForTK(String newToken, TaiKhoan taiKhoan) {
        VerificationToken foundToken = verificationTokenRepository.findByTaiKhoan(taiKhoan);
        foundToken.setToken(newToken);
        foundToken.setExpirationTime((new VerificationToken("")).getExpirationTime());
        verificationTokenRepository.save(foundToken);
    }

    public void createNewAccount(TaiKhoan taiKhoan) {
        taiKhoan.setState(new UnverifiedState());
        taiKhoanRepository.save(taiKhoan);
    }
}
