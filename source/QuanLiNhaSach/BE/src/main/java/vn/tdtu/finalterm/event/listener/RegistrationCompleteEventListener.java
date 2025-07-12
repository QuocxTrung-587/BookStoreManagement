package vn.tdtu.finalterm.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vn.tdtu.finalterm.config.EmailSenderService;
import vn.tdtu.finalterm.enums.DefaultValues;
import vn.tdtu.finalterm.event.RegistrationCompleteEvent;
import vn.tdtu.finalterm.models.TaiKhoan;
import vn.tdtu.finalterm.service.TaiKhoanService;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {
    @Autowired
    private TaiKhoanService taiKhoanService;
    @Autowired
    private EmailSenderService service;

    @Override
    @Async
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        TaiKhoan taiKhoan = event.getTaiKhoan();
        String token = UUID.randomUUID().toString();
        taiKhoanService.saveVerificationTokenForTK(token, taiKhoan);

        String FEURL = DefaultValues.FRONTEND_URL.getUrl();
        // Send Mail to user
        String url = FEURL.isEmpty() ? event.getApplicationUrl() + "/verifyRegistration?token=" + token : FEURL + "?token=" + token;

        //sendVerificationEmail
//        log.info("Click the link to verify your account: {}", url);
        triggerMail(taiKhoan, event.getPasswordPrimitive() , url);
    }

    public void triggerMail(TaiKhoan taiKhoan, String passwordPrimitive, String url) {
        service.sendEmail(taiKhoan.getTaiKhoan(),
                "Please click on the verification link " + url + "\nThis is your password: " + passwordPrimitive,
                "Fahasa's sub-branch account verification email");
    }
}
