package recruiter.Job.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import recruiter.Job.Util.*;
import recruiter.Job.DTO.RecruiterRegistrationDTO;
import recruiter.Job.Entity.Recruiter;
import recruiter.Job.Repository.RecruiterRepository;
import recruiter.Job.Service.EmailService;
import recruiter.Job.Service.RecruiterService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RecruiterServiceImpl implements RecruiterService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private final int OTP_EXPIRY_MINUTES = 10;

    @Override
    public Recruiter register(RecruiterRegistrationDTO dto) {
        if (recruiterRepository.findByUsername(dto.getUsername()).isPresent())
            throw new RuntimeException("Username already exists");
        if (recruiterRepository.findByEmail(dto.getEmail()).isPresent())
            throw new RuntimeException("Email already exists");

        Recruiter r = new Recruiter();
        r.setCompanyName(dto.getCompanyName());
        r.setUsername(dto.getUsername());
        r.setPassword(passwordEncoder.encode(dto.getPassword()));
        r.setEmail(dto.getEmail());
        r.setPhoneNumber(dto.getPhoneNumber());
        r.setVerified(false);

        String otp = generateOtp();
        r.setOtp(otp);
        r.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        Recruiter saved = recruiterRepository.save(r);
        emailService.sendOtpEmail(saved.getEmail(), otp);

        return saved;
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        Optional<Recruiter> opt = recruiterRepository.findByEmail(email);
        if (opt.isEmpty()) return false;
        Recruiter r = opt.get();
        if (r.getOtp() != null && r.getOtp().equals(otp) && LocalDateTime.now().isBefore(r.getOtpExpiryDate())) {
            r.setOtp(null);
            r.setOtpExpiryDate(null);
            r.setVerified(true);
            recruiterRepository.save(r);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Recruiter> findByUsername(String username) {
        return recruiterRepository.findByUsername(username);
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public void initiatePasswordReset(String email) {
        Optional<Recruiter> opt = recruiterRepository.findByEmail(email);
        if (opt.isEmpty()) throw new RuntimeException("Recruiter not found with this email");
        Recruiter r = opt.get();
        String otp = generateOtp();
        r.setOtp(otp);
        r.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        recruiterRepository.save(r);
        emailService.sendPasswordOtp(r.getEmail(), otp);
    }

    @Override
    public void verifyOtpAndResetPassword(String otp, String newPassword) {
        Recruiter r = recruiterRepository.findByOtp(otp);
        if (r == null || r.getOtpExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Invalid or expired OTP");
        r.setPassword(passwordEncoder.encode(newPassword));
        r.setOtp(null);
        r.setOtpExpiryDate(null);
        recruiterRepository.save(r);
    }
    
    @Override
    public Recruiter getUserDetailsFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return recruiterRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    
    @Override
    public Recruiter getRecruiterProfile(Long recruiterId) {
        return recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

