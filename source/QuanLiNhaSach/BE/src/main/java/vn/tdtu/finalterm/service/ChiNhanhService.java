package vn.tdtu.finalterm.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.event.AccountSessionScope;
import vn.tdtu.finalterm.event.RegistrationCompleteEvent;
import vn.tdtu.finalterm.models.ChiNhanh;
import vn.tdtu.finalterm.models.ResponseObject;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.ChiNhanhRepository;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;
import vn.tdtu.finalterm.repositories.VerificationTokenRepository;
import vn.tdtu.finalterm.service.states.UnverifiedState;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Service
public class ChiNhanhService implements SearchStrategy{
    @Autowired
    private ChiNhanhRepository chiNhanhRepository;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private AccountSessionScope accountSessionScope;

    public ResponseEntity<ResponseObject> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query All ChiNhanh Success", chiNhanhRepository.findAll())
        );
    }

    public ResponseEntity<ResponseObject> findChiNhanhById(Long chiNhanhId) {
        Optional<ChiNhanh> chiNhanh = chiNhanhRepository.findById(chiNhanhId);

        return chiNhanh.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query ChiNhanh Success", chiNhanh)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find ChiNhanh with id = " + chiNhanhId, "")
                );
    }

    public ResponseEntity<ResponseObject> insertChiNhanh(ChiNhanh chiNhanh, HttpServletRequest request) {
        List<ChiNhanh> foundChiNhanh = chiNhanhRepository.findByTenChiNhanh(chiNhanh.getTenChiNhanh().trim());
        // If the same name
        if(foundChiNhanh.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "tenChiNhanh already taken or the same id", "")
            );
        }

        List<ChiNhanh> foundChiNhanhByEmail = chiNhanhRepository.findByEmail(chiNhanh.getEmail().trim());
        // If the same email
        if(foundChiNhanhByEmail.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Email already taken", "")
            );
        }

        // Check Regex Email
        if(!Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$").matcher(chiNhanh.getEmail()).matches()) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Please enter the correct email structure", "")
            );
        }

        // Save first to take Id
        ChiNhanh res = chiNhanhRepository.save(chiNhanh);

        String passwordPrimitive = Integer.toString(new Random().nextInt(90000) + 10000);
        // Create new Account
        TaiKhoan taiKhoan = new TaiKhoan(res.getEmail(),
                passwordEncoder.encode(passwordPrimitive),
                false,
                null);
        taiKhoan.setState(new UnverifiedState()); // Set initial state
        
        // Push to TaiKhoan Database
        taiKhoanRepository.save(taiKhoan);

        // Map to this ChiNhanh
        res.setTaiKhoanFK(taiKhoan);

        // Save to session
        accountSessionScope.setTaiKhoan(new TaiKhoan(res.getEmail(),
                passwordPrimitive,
                false,
                null));

        // Asynchronous activation (Send mail)
        publisher.publishEvent(new RegistrationCompleteEvent(
                taiKhoan,
                passwordPrimitive,
                "http://"
                        + request.getServerName() + ":"
                        + request.getServerPort()
                        + request.getContextPath()
        ));

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert ChiNhanh and TaiKhoan Success", chiNhanhRepository.save(res))
        );
    }

    public ResponseEntity<ResponseObject> updateChiNhanh(ChiNhanh newChiNhanh, Long id) {
        List<ChiNhanh> foundChiNhanh = chiNhanhRepository.findByTenChiNhanh(newChiNhanh.getTenChiNhanh().trim());
        if(foundChiNhanh.size() > 0) {
            // Trường hợp update nhưng tên chi nhánh trùng thì chỉ cho thay đổi field khi trùng id
            if(foundChiNhanh.get(0).getId() != id) {
                return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                        new ResponseObject("failed", "tenChiNhanh already taken", "")
                );
            }
        }

        ChiNhanh updatedChiNhanh = chiNhanhRepository.findById(id)
                .map(chiNhanh -> {
                    chiNhanh.setTenChiNhanh(newChiNhanh.getTenChiNhanh());
                    chiNhanh.setDiaChi(newChiNhanh.getDiaChi());

                    return chiNhanhRepository.save(chiNhanh);
                }).orElseGet(() -> {
                    return null;
                });

        if(updatedChiNhanh == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh with id = " + id, "")
        );

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update ChiNhanh Success", updatedChiNhanh)
        );
    }

    @Transactional
    public ResponseEntity<ResponseObject> deleteChiNhanh(Long id) {
        Optional<ChiNhanh> foundCN = chiNhanhRepository.findById(id);
        if(!foundCN.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Cannot find ChiNhanh to delete", "")
            );
        }

        Optional<TaiKhoan> foundTK = taiKhoanRepository.findById(foundCN.get().getTaiKhoanFK().getTaiKhoan());
        if(!foundTK.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Cannot find TaiKhoan to delete", "")
            );
        }

//        if(!foundTK.get().isEnabled()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//                    new ResponseObject("failed", "Can't delete unauthenticated TaiKhoan", "")
//            );
//        }

        if(foundCN.isPresent()) {
            verificationTokenRepository.deleteByTaiKhoan(foundTK.get());
            chiNhanhRepository.deleteById(id);
            taiKhoanRepository.deleteById(foundTK.get().getTaiKhoan());

            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete ChiNhanh Success", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find ChiNhanh to delete", "")
        );
    }

    public ResponseEntity<ResponseObject> findChiNhanhByTaiKhoan(TaiKhoan taiKhoan) {
        Optional<TaiKhoan> foundTaiKhoan = taiKhoanRepository.findById(taiKhoan.getTaiKhoan());
        if(!foundTaiKhoan.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("ok", "Cannot find TaiKhoan with taiKhoan = " + taiKhoan.getTaiKhoan(), "")
        );

        Optional<ChiNhanh> foundCN = chiNhanhRepository.findByTaiKhoanFK(foundTaiKhoan.get());

        return foundCN.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query ChiNhanh Success", foundCN)
                ) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new ResponseObject("failed", "Cannot find ChiNhanh with taiKhoan = " + taiKhoan.getTaiKhoan(), "")
                );
    }

    public ResponseEntity<ResponseObject> findCNByTenOrDiaChi(String key) {
        List<ChiNhanh> foundCN = chiNhanhRepository.findByTenChiNhanhContainsOrDiaChiContains(key, key);

        return ResponseEntity.status(HttpStatus.OK).body(
                        new ResponseObject("ok", "Query ChiNhanh Success", foundCN)
        );
    }

    public ResponseEntity<ResponseObject> findCNByTen(String key) {
        List<ChiNhanh> foundCN = chiNhanhRepository.findByTenChiNhanhContains(key);

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Query ChiNhanh Success", foundCN)
        );
    }
}
