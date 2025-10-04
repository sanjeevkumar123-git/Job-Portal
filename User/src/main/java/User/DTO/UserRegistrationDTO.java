package User.DTO;

import org.springframework.web.multipart.MultipartFile;

public class UserRegistrationDTO {
	 private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private MultipartFile profilePicture; 
    private String linkedinUrl;
    private String githubUrl;
    private String projectsDeployedUrl;
    private String interestedIn;
    private String skills; 
    private String currentEmploymentStatus;
    private String[] experience; 
    private String[] certifications;
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
	public MultipartFile getProfilePicture() {
		return profilePicture;
	}
	public void setProfilePicture(MultipartFile profilePicture) {
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
    
	
	
    
    // Getters and Setters
    
    
}
