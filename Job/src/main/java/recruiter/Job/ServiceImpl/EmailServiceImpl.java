package recruiter.Job.ServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import recruiter.Job.Service.EmailService;
 
@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        String subject = "Your Recruiter Registration OTP";
        String body = "Your OTP is: " + otp + "\nIt will expire in 10 minutes.";
        sendMail(to, subject, body);
    }

    @Override
    public void sendPasswordOtp(String to, String otp) {
        String subject = "Password Reset OTP";
        String body = "Your password reset OTP is: " + otp + "\nIt will expire in 10 minutes.";
        sendMail(to, subject, body);
    }

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}

