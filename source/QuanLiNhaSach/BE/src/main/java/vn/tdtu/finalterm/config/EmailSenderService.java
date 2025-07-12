package vn.tdtu.finalterm.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String body, String title) {
        SimpleMailMessage message = new SimpleMailMessage(  );

        message.setFrom("this.is.manager.fahasa@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(title);

        mailSender.send(message);
        log.info("Mail send to {}", toEmail);
    }
}
