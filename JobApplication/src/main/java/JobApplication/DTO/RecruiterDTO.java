package JobApplication.DTO;



import lombok.Data;

@Data
public class RecruiterDTO {
    private Long id;
    private String companyName;
    private String username;
    private String email;
    private String phoneNumber;
    private boolean verified;
	public RecruiterDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RecruiterDTO(Long id, String companyName, String username, String email, String phoneNumber,
			boolean verified) {
		super();
		this.id = id;
		this.companyName = companyName;
		this.username = username;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.verified = verified;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
    
    
}

