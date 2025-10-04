package recruiter.Job.Service;



import java.util.Optional;


import recruiter.Job.DTO.RecruiterRegistrationDTO;
import recruiter.Job.Entity.Recruiter;

public interface RecruiterService {
    Recruiter register(RecruiterRegistrationDTO dto);
    boolean verifyOtp(String email, String otp);
    Optional<Recruiter> findByUsername(String username);
    void initiatePasswordReset(String email);
    void verifyOtpAndResetPassword(String otp, String newPassword);
    Recruiter getUserDetailsFromToken(String jwtToken);
	Recruiter getRecruiterProfile(Long recruiterId);
}

