package recruiter.Job.Entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recruiter")
public class Recruiter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    @Column(unique = true, nullable = false)
    private String username;
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    private String phoneNumber;

    private boolean verified = false;
    private String otp;
    private LocalDateTime otpExpiryDate;

    // getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }
    public LocalDateTime getOtpExpiryDate() { return otpExpiryDate; }
    public void setOtpExpiryDate(LocalDateTime otpExpiryDate) { this.otpExpiryDate = otpExpiryDate;
    
    
    
    
    }
	public Recruiter(Long id, String companyName, String username, String password, String email, String phoneNumber,
			boolean verified, String otp, LocalDateTime otpExpiryDate) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.username = username;
		this.password = password;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.verified = verified;
		this.otp = otp;
		this.otpExpiryDate = otpExpiryDate;
	}
	public Recruiter() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}

