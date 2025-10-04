package User.ServiceImpl;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import User.DTO.ResumeDTO;
import User.DTO.UserLoginDTO;
import User.DTO.UserRegistrationDTO;
import User.Entity.Resume;
import User.Entity.User;
import User.Exceptions.ResourceNotFoundException;
import User.Repository.ResumeRepository;
import User.Repository.UserRepository;
import User.Security.JwtTokenUtil;
import User.Service.EmailService;
import User.Service.UserService;

@Service
public class UserServiceImpl implements UserService , UserDetailsService  {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResumeRepository resumeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private EmailService emailService;
    
    private final int OTP_EXPIRY_MINUTES = 10;

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        if (userRepository.findByUsername(dto.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        
        if (userRepository.findByEmail(dto.getEmail()) != null) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setGithubUrl(dto.getGithubUrl());
        user.setProjectsDeployedUrl(dto.getProjectsDeployedUrl());
        user.setInterestedIn(dto.getInterestedIn());
        user.setSkills(dto.getSkills());
        user.setCurrentEmploymentStatus(dto.getCurrentEmploymentStatus());
        user.setExperience(dto.getExperience());
        user.setCertifications(dto.getCertifications());

        if (dto.getProfilePicture() != null) {
            try {
                user.setProfilePicture(dto.getProfilePicture().getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile picture data.", e);
            }
        }
        
        user.setVerified(false);

        // Generate OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        User savedUser = userRepository.save(user);

        // Send OTP email
        emailService.sendOtpEmail(savedUser.getEmail(), otp);

        // Return the saved user
        return savedUser;
    }
    
    
    @Override
    public boolean verifyOtp(String email, String otp) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getOtp().equals(otp) && LocalDateTime.now().isBefore(user.getOtpExpiryDate())) {
                // OTP is valid, update user
                user.setOtp(null); // Clear OTP
                user.setOtpExpiryDate(null);
                user.setVerified(true); // Mark as verified

                // Move user to final storage
                userRepository.save(user);
                return true;
            } else {
                return false;
            }
        } else {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }
    }

    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
    
    
    @Override
    public User updateUser(Long userId, UserRegistrationDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setAddress(dto.getAddress());
        user.setLinkedinUrl(dto.getLinkedinUrl());
        user.setGithubUrl(dto.getGithubUrl());
        user.setProjectsDeployedUrl(dto.getProjectsDeployedUrl());
        user.setInterestedIn(dto.getInterestedIn());
        user.setSkills(dto.getSkills());
        user.setCurrentEmploymentStatus(dto.getCurrentEmploymentStatus());
        user.setExperience(dto.getExperience());
        user.setCertifications(dto.getCertifications());

        if (dto.getProfilePicture() != null) {
            try {
                user.setProfilePicture(dto.getProfilePicture().getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile picture data.", e);
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(userId);
    }
    
    
    @Override
    public User loginUser(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername());
        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return user;
        }
        throw new RuntimeException("Invalid username or password");
    }

    public Resume uploadResume(Long userId, ResumeDTO resumeDTO, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Resume resume = new Resume();
        resume.setResumeName(resumeDTO.getResumeName());
        try {
			resume.setData(file.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        resume.setUploadDate(LocalDate.now());
        resume.setUser(user);

        user.getResumes().add(resume);
        userRepository.save(user);

        return resume;
    }

    @Override
    public List<Resume> getResumesByUserId(Long userId) {
        return resumeRepository.findByUserId(userId);
    }

    @Override
    public Resume getResumeById(Long resumeId) {
        return resumeRepository.findById(resumeId)
            .orElseThrow(() -> new RuntimeException("Resume not found"));
    }
    
    
    @Override
    public User getUserProfile(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public void initiatePasswordReset(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        User user = optionalUser.get();

        // Generate OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        userRepository.save(user);

        // Send OTP email
        emailService.sendPasswordOtp(user.getEmail(), otp);
    }

    @Override
    public void verifyOtpAndResetPassword(String otp, String newPassword) {
        User user = userRepository.findByOtp(otp);
        if (user == null || user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invalid or expired OTP.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setOtp(null); // Clear OTP
        user.setOtpExpiryDate(null); // Clear expiry date
        userRepository.save(user);
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Old password is incorrect.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    

    @Override
    public String login(UserLoginDTO dto) {
        User user = userRepository.findByUsername(dto.getUsername());
        if (user != null && passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            return jwtTokenUtil.generateToken(user.getUsername());
        }
        throw new RuntimeException("Invalid username or password");
    }

    
    
    

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(), 
            user.getPassword(),     
            new ArrayList<>() // Assuming no roles/authorities, modify as needed
        );
    }
    
    
    
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


//	@Override
//	public String login(String username, String password) {
//		// TODO Auto-generated method stub
//		return null;
//	}


}
