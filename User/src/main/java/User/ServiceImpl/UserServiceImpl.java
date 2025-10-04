package User.ServiceImpl;

import java.security.SecureRandom;
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

import User.DTO.UserRegistrationDTO;
import User.Entity.User;
import User.Exceptions.ResourceNotFoundException;
import User.Repository.UserRepository;
import User.Service.EmailService;
import User.Service.UserService;
import User.Util.JwtUtil;
import recruiter.Job.Entity.Recruiter;

@Service
public class UserServiceImpl implements UserService , UserDetailsService  {

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private ResumeRepository resumeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    @Autowired
    private JwtUtil jwtUtil;

    

    
    


    @Autowired
    private EmailService emailService;
    
    private final int OTP_EXPIRY_MINUTES = 10;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
    
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User registerUser(UserRegistrationDTO dto) {
    	if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
    	    throw new RuntimeException("Username already exists");
    	}

    	if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
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

//        
       user.setVerified(false);
//
        // Generate OTP
        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        User savedUser = userRepository.save(user);

        // Send OTP email
        emailService.sendOtpEmail(savedUser.getEmail(), savedUser.getOtp());

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
        Optional<User> opt = userRepository.findByEmail(email);
        if (opt.isEmpty()) throw new RuntimeException("Recruiter not found with this email");
        User r = opt.get();
        String otp = generateOtp();
        r.setOtp(otp);
        r.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));
        userRepository.save(r);
        emailService.sendPasswordOtp(r.getEmail(), otp);
    }

    @Override
    public void verifyOtpAndResetPassword(String otp, String newPassword) {
        User r = userRepository.findByOtp(otp);
        if (r == null || r.getOtpExpiryDate().isBefore(LocalDateTime.now()))
            throw new RuntimeException("Invalid or expired OTP");
        r.setPassword(passwordEncoder.encode(newPassword));
        r.setOtp(null);
        r.setOtpExpiryDate(null);
        userRepository.save(r);
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
    public User getUserDetailsFromToken(String token) {
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

}
