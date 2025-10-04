package User.Controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import User.DTO.LoginResponseDTO;
import User.DTO.ResetPasswordDTO;
import User.DTO.UserLoginDTO;
import User.DTO.UserRegistrationDTO;
import User.Entity.User;
import User.Repository.UserRepository;
import User.Service.UserService;
import User.Util.JwtUtil;


@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private UserRepository userRepository;
    

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO dto) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Incorrect username or password");
        }

        // check verified
        User r = userService.findByUsername(dto.getUsername())
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));

        if (!r.isVerified()) {
            return ResponseEntity.status(401).body("Please verify OTP before login");
        }

        String token = jwtUtil.generateToken(r.getUsername());
        return ResponseEntity.ok(new LoginResponseDTO(token, r.getUsername(), r.getEmail() , r.getId()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("Registration initiated. Please check your email for OTP.");
    }

  
    
    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserRegistrationDTO dto) {
        User updatedUser = userService.updateUser(userId, dto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/All")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
//
//    @PostMapping("/{userId}/upload-resume")
//    public ResponseEntity<?> uploadResume(
//            @PathVariable Long userId,
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("resumeName") String resumeName) {
//
//        try {
//            if (file.isEmpty()) {
//                return ResponseEntity.badRequest().body("File is empty");
//            }
//
//            ResumeDTO resumeDTO = new ResumeDTO();
//            resumeDTO.setResumeName(resumeName);
//
//            Resume resume = userService.uploadResume(userId, resumeDTO, file);
//            return ResponseEntity.ok(resume);
//        } catch (Exception e) {
//            e.printStackTrace(); // Logs error in backend console
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Failed to upload resume: " + e.getMessage());
//        }
//    }

    
    @GetMapping("/{userId}/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

//    @GetMapping("/{userId}/resumes")
//    public ResponseEntity<List<Resume>> getResumesByUserId(@PathVariable Long userId) {
//        List<Resume> resumes = userService.getResumesByUserId(userId);
//        return ResponseEntity.ok(resumes);
//    }

//    @GetMapping("/resumes/download/{resumeId}")
//    public ResponseEntity<Resource> downloadResume(@PathVariable Long resumeId) {
//        Resume resume = userService.getResumeById(resumeId);
//        byte[] data = resume.getData();
//        ByteArrayInputStream bis = new ByteArrayInputStream(data);
//
//        InputStreamResource resource = new InputStreamResource(bis);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getResumeName() + "\"")
//                .body(resource);
//    }

//    @GetMapping("/resumes/view/{resumeId}")
//    public ResponseEntity<Resource> viewResume(@PathVariable Long resumeId) {
//        Resume resume = userService.getResumeById(resumeId);
//        byte[] data = resume.getData();
//        ByteArrayInputStream bis = new ByteArrayInputStream(data);
//
//        InputStreamResource resource = new InputStreamResource(bis);
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean ok = userService.verifyOtp(email, otp);
        if (ok) return ResponseEntity.ok("Verified successfully");
        return ResponseEntity.badRequest().body("Invalid or expired OTP");
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("OTP sent to email");
    }

    // reset password using OTP
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto) {
        try {
            userService.verifyOtpAndResetPassword(dto.getOtp(), dto.getNewPassword());
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok("Password has been successfully changed.");
    }
    
    @GetMapping("/details")
    public ResponseEntity<User> getUserDetails(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7); // Remove "Bearer " prefix
        User userDetails = userService.getUserDetailsFromToken(jwtToken);
        return ResponseEntity.ok(userDetails);
    }
    
    


}

