package JobApplication.Service;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

	@Autowired
    private  JavaMailSender mailSender;

    public void sendJobPostedEmail(String toEmail, String recruiterName, String jobTitle, String companyName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toEmail);
            helper.setSubject("Job Posted Successfully - " + jobTitle);
            helper.setText(
                    "Hello " + recruiterName + ",\n\n" +
                    "Your job posting has been created successfully!\n\n" +
                    "Job Title: " + jobTitle + "\n" +
                    "Company: " + companyName + "\n\n" +
                    "Best Regards,\nJob Portal Team"
            );

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}

