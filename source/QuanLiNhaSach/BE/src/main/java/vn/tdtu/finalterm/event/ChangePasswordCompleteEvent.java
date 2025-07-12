package vn.tdtu.finalterm.event;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import vn.tdtu.finalterm.models.TaiKhoan;

@Getter
@Setter
public class ChangePasswordCompleteEvent extends ApplicationEvent {
    private final TaiKhoan taiKhoan;
    private final String passwordPrimitive;
    private final String applicationUrl;

    public ChangePasswordCompleteEvent(TaiKhoan taiKhoan, String passwordPrimitive, String applicationUrl) {
        super(taiKhoan);
        this.taiKhoan = taiKhoan;
        this.passwordPrimitive = passwordPrimitive;
        this.applicationUrl = applicationUrl;
    }
}
