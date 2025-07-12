package vn.tdtu.finalterm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.repositories.TaiKhoanRepository;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private static UserService instance;
    private final TaiKhoanRepository taiKhoanRepository;

    private UserService(TaiKhoanRepository taiKhoanRepository) {
        this.taiKhoanRepository = taiKhoanRepository;
    }

    public static UserService getInstance(TaiKhoanRepository repository) {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService(repository);
                }
            }
        }
        return instance;
    }

    @Override
    public UserDetails loadUserByUsername(String taiKhoanName) throws UsernameNotFoundException {
        TaiKhoan foundTK = taiKhoanRepository.findById(taiKhoanName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(foundTK.getTaiKhoan(), foundTK.getMatKhau(), new ArrayList<>());
    }
}

