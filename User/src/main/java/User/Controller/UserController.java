package User.Controller;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.multipart.MultipartFile;

import User.DTO.ResumeDTO;
import User.DTO.UserLoginDTO;
import User.DTO.UserRegistrationDTO;
import User.Entity.Resume;
import User.Entity.User;
import User.Service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.ok("Registration initiated. Please check your email for OTP.");
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody UserLoginDTO dto) {
//        try {
//            @SuppressWarnings("unused")
//			User user = userService.loginUser(dto);
//            return ResponseEntity.ok("Login successful!");
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//        }
//    }
//    
    
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

    @PostMapping("/{userId}/upload-resume")
    public ResponseEntity<Resume> uploadResume(@PathVariable Long userId, @RequestParam("file") MultipartFile file, @RequestParam("resumeName") String resumeName) {
        ResumeDTO resumeDTO = new ResumeDTO();
        resumeDTO.setResumeName(resumeName);

        Resume resume = userService.uploadResume(userId, resumeDTO, file);
        return ResponseEntity.ok(resume);
    }
    
    @GetMapping("/{userId}/profile")
    public ResponseEntity<User> getUserProfile(@PathVariable Long userId) {
        User user = userService.getUserProfile(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/resumes")
    public ResponseEntity<List<Resume>> getResumesByUserId(@PathVariable Long userId) {
        List<Resume> resumes = userService.getResumesByUserId(userId);
        return ResponseEntity.ok(resumes);
    }

    @GetMapping("/resumes/download/{resumeId}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long resumeId) {
        Resume resume = userService.getResumeById(resumeId);
        byte[] data = resume.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        InputStreamResource resource = new InputStreamResource(bis);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getResumeName() + "\"")
                .body(resource);
    }

    @GetMapping("/resumes/view/{resumeId}")
    public ResponseEntity<Resource> viewResume(@PathVariable Long resumeId) {
        Resume resume = userService.getResumeById(resumeId);
        byte[] data = resume.getData();
        ByteArrayInputStream bis = new ByteArrayInputStream(data);

        InputStreamResource resource = new InputStreamResource(bis);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isVerified = userService.verifyOtp(email, otp);
        if (isVerified) {
            return ResponseEntity.ok("Email verified successfully. You can now log in.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid OTP or OTP expired.");
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        userService.initiatePasswordReset(email);
        return ResponseEntity.ok("OTP has been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String otp, @RequestParam String newPassword) {
        userService.verifyOtpAndResetPassword(otp, newPassword);
        return ResponseEntity.ok("Password has been successfully reset.");
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestParam Long userId, @RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok("Password has been successfully changed.");
    }
    
    
    
    @PostMapping("/login")
    public String loginUser(@RequestBody UserLoginDTO dto) {
        return userService.login(dto);
    }
    
    
    
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username, 
                                                  @RequestHeader("Authorization") String authHeader) {
        // You can include authorization checks here if necessary
        User user = userService.findUserByUsername(username);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

