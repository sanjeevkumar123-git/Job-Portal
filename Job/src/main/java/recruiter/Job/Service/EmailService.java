package recruiter.Job.Service;



public interface EmailService {
    void sendOtpEmail(String to, String otp);
    void sendPasswordOtp(String to, String otp);
}
