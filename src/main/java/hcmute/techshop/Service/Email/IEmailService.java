package hcmute.techshop.Service.Email;

public interface IEmailService {
    void sendMailRegister(String toEmail, String code);
    void sendMailForgotPassword(String toEmail,String token);
}
