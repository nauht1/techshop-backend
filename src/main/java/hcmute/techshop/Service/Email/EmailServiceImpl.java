package hcmute.techshop.Service.Email;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.util.IOUtils;

import hcmute.techshop.Exception.BadRequestException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements IEmailService{

    @Value("spring.mail.username")
    private String fromMail;

    private final JavaMailSender javaMailSender;

    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Override
    public void sendMailRegister(String toEmail, String code) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/SendMailRegisterFile.html");
            InputStream inputStream = resource.getInputStream();
            String htmlContent = IOUtils.readInputStreamToString(inputStream, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("{{code}}", code);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(fromMail);
            helper.setTo(toEmail);
            helper.setSubject("Confirm code");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        } catch (IOException | MessagingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Override
    public void sendMailForgotPassword(String toEmail, String token) {
        try {
            ClassPathResource resource = new ClassPathResource("templates/SendMailForgotPassword.html");
            InputStream inputStream = resource.getInputStream();
            String htmlContent = IOUtils.readInputStreamToString(inputStream, StandardCharsets.UTF_8);

            htmlContent = htmlContent.replace("YOUR_TOKEN", token);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(fromMail);
            helper.setTo(toEmail);
            helper.setSubject("Forgot password");
            helper.setText(htmlContent, true);
            javaMailSender.send(mimeMessage);
        }catch (IOException | MessagingException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}