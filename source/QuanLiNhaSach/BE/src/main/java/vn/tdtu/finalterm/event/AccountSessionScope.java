package vn.tdtu.finalterm.event;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import vn.tdtu.finalterm.models.TaiKhoan;

@Component
@SessionScope
@Data
public class AccountSessionScope {
    private TaiKhoan taiKhoan;
}
