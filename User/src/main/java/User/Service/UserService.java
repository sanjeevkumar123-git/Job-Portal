package User.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import User.DTO.LoginResponseDTO;
import User.DTO.UserLoginDTO;
import User.DTO.UserRegistrationDTO;
import User.Entity.User;

public interface UserService extends UserDetailsService {
	
	
    User registerUser(UserRegistrationDTO dto);

//    List<Resume> getResumesByUserId(Long userId);
//    
//	Resume uploadResume(Long userId, ResumeDTO dto, MultipartFile file);
//	
//	Resume getResumeById(Long resumeId);
	
	User getUserProfile(Long userId);
	
	void deleteUser(Long userId);
	
	User updateUser(Long userId, UserRegistrationDTO dto);
	
	List<User> getAllUsers();

	boolean verifyOtp(String email, String otp);
	
	void initiatePasswordReset(String email);
	
	void verifyOtpAndResetPassword(String otp, String newPassword);
	
	void changePassword(Long userId, String oldPassword, String newPassword);
	
	Optional<User> findByUsername(String username);
	 
	User getUserDetailsFromToken(String jwtToken);

	LoginResponseDTO login(UserLoginDTO loginDTO);
	
	
}

