package User.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password; // Hashed password
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    
    @Lob
    private byte[] profilePicture; // Store profile picture as byte array

    private String linkedinUrl; // LinkedIn profile URL
    private String githubUrl; // GitHub profile URL
    private String projectsDeployedUrl; // URL for projects deployed
    private String interestedIn; // What the user is interested in
    private String skills; // Comma-separated list of skills
    private String currentEmploymentStatus; // e.g., Employed, Seeking Opportunities, etc.
    private String[] experience; // Array or List of experiences
    private String[] certifications;
    
    private String otp;
    private LocalDateTime otpExpiryDate;
    private boolean isVerified;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<Resume> getResumes() {
		return resumes;
	}

	public void setResumes(List<Resume> resumes) {
		this.resumes = resumes;
	}
	
	

	public byte[] getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(byte[] profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getLinkedinUrl() {
		return linkedinUrl;
	}

	public void setLinkedinUrl(String linkedinUrl) {
		this.linkedinUrl = linkedinUrl;
	}

	public String getGithubUrl() {
		return githubUrl;
	}

	public void setGithubUrl(String githubUrl) {
		this.githubUrl = githubUrl;
	}

	public String getProjectsDeployedUrl() {
		return projectsDeployedUrl;
	}

	public void setProjectsDeployedUrl(String projectsDeployedUrl) {
		this.projectsDeployedUrl = projectsDeployedUrl;
	}

	public String getInterestedIn() {
		return interestedIn;
	}

	public void setInterestedIn(String interestedIn) {
		this.interestedIn = interestedIn;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getCurrentEmploymentStatus() {
		return currentEmploymentStatus;
	}

	public void setCurrentEmploymentStatus(String currentEmploymentStatus) {
		this.currentEmploymentStatus = currentEmploymentStatus;
	}

	public String[] getExperience() {
		return experience;
	}

	public void setExperience(String[] experience) {
		this.experience = experience;
	}

	public String[] getCertifications() {
		return certifications;
	}

	public void setCertifications(String[] certifications) {
		this.certifications = certifications;
	}

	
	



	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public LocalDateTime getOtpExpiryDate() {
		return otpExpiryDate;
	}

	public void setOtpExpiryDate(LocalDateTime otpExpiryDate) {
		this.otpExpiryDate = otpExpiryDate;
	}
	
	

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public User(Long id, String username, String password, String email, String firstName, String lastName,
			String phoneNumber, String address, byte[] profilePicture, String linkedinUrl, String githubUrl,
			String projectsDeployedUrl, String interestedIn, String skills, String currentEmploymentStatus,
			String[] experience, String[] certifications, List<Resume> resumes) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.profilePicture = profilePicture;
		this.linkedinUrl = linkedinUrl;
		this.githubUrl = githubUrl;
		this.projectsDeployedUrl = projectsDeployedUrl;
		this.interestedIn = interestedIn;
		this.skills = skills;
		this.currentEmploymentStatus = currentEmploymentStatus;
		this.experience = experience;
		this.certifications = certifications;
		this.resumes = resumes;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

    // Getters and Setters
    
    
    
    
}

