package vn.tdtu.finalterm.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vn.tdtu.finalterm.config.EmailSenderService;
import vn.tdtu.finalterm.event.ChangePasswordCompleteEvent;
import vn.tdtu.finalterm.event.RegistrationCompleteEvent;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.service.TaiKhoanService;

import java.util.UUID;

@Component
@Slf4j
public class ChangePasswordCompleteEventListener implements
        ApplicationListener<ChangePasswordCompleteEvent> {
    @Autowired
    private EmailSenderService service;

    @Override
    @Async
    public void onApplicationEvent(ChangePasswordCompleteEvent event) {
        TaiKhoan taiKhoan = event.getTaiKhoan();

        //sendVerificationEmail
//        log.info("Click the link to verify your account: {}", url);
        triggerMail(taiKhoan, event.getPasswordPrimitive());
    }

    public void triggerMail(TaiKhoan taiKhoan, String passwordPrimitive) {
        service.sendEmail(taiKhoan.getTaiKhoan(),
                "This is your new password: " + passwordPrimitive,
                "Fahasa's sub-branch account change password");
    }
}
