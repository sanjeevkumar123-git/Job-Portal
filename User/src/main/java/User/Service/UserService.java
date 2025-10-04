package User.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import User.DTO.ResumeDTO;
import User.DTO.UserLoginDTO;
import User.DTO.UserRegistrationDTO;
import User.Entity.Resume;
import User.Entity.User;

public interface UserService {
    User registerUser(UserRegistrationDTO dto);
    User loginUser(UserLoginDTO dto);
//    Resume uploadResume(Long userId, ResumeDTO dto);
    List<Resume> getResumesByUserId(Long userId);
	Resume uploadResume(Long userId, ResumeDTO dto, MultipartFile file);
	Resume getResumeById(Long resumeId);
	User getUserProfile(Long userId);
	void deleteUser(Long userId);
	User updateUser(Long userId, UserRegistrationDTO dto);
	List<User> getAllUsers();
//	boolean verifyEmail(String token);
	boolean verifyOtp(String email, String otp);
	void initiatePasswordReset(String email);
	void verifyOtpAndResetPassword(String otp, String newPassword);
	void changePassword(Long userId, String oldPassword, String newPassword);
//	String login(String username, String password);
	String login(UserLoginDTO dto);
	User findUserByUsername(String username);
}

