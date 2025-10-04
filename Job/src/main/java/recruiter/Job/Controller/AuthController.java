package recruiter.Job.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import recruiter.Job.DTO.LoginResponseDTO;
import recruiter.Job.DTO.RecruiterLoginDTO;
import recruiter.Job.DTO.RecruiterRegistrationDTO;
import recruiter.Job.DTO.ResetPasswordDTO;
import recruiter.Job.Entity.Recruiter;
import recruiter.Job.Service.RecruiterService;
import recruiter.Job.Util.JwtUtil;

@RestController
@RequestMapping("/api/recruiter/auth")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class AuthController {

    @Autowired
    private RecruiterService recruiterService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    // register
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RecruiterRegistrationDTO dto) {
        Recruiter saved = recruiterService.register(dto);
        return ResponseEntity.ok("Registered. Please verify OTP sent to email.");
    }

    // verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean ok = recruiterService.verifyOtp(email, otp);
        if (ok) return ResponseEntity.ok("Verified successfully");
        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RecruiterLoginDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        // check verified
        Recruiter r = recruiterService.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (!r.isVerified()) {
            return ResponseEntity.status(401).body("Please verify OTP before login");
        }

        String token = jwtUtil.generateToken(r.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(token, r.getUsername(), r.getEmail()));
    }

    // forgot password (send OTP)
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        recruiterService.initiatePasswordReset(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    // reset password using OTP
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        try {
            recruiterService.verifyOtpAndResetPassword(dto.getOtp(), dto.getNewPassword());
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    @GetMapping("/{recruiterId}/profile")
    public ResponseEntity<Recruiter> getUserProfile(@PathVariable Long recruiterId) {
        Recruiter user = recruiterService.getRecruiterProfile(recruiterId);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/details")
    public ResponseEntity<Recruiter> getUserDetails(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        Recruiter userDetails = recruiterService.getUserDetailsFromToken(jwtToken);
        return ResponseEntity.ok(userDetails);
    }
    
}

