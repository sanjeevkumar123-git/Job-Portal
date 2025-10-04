package JobApplication.DTO;

import java.time.LocalDate;

import JobApplication.ApplicationStatus;

public class ApplicantDTO {
    private Long applicationId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate appliedDate;
    private String resumeFileName;
    private byte[] resume;
    private ApplicationStatus status; 
    // constructor, getters, setters
  
	public ApplicantDTO() {
		super();
		// TODO Auto-generated constructor stub
	}



	public ApplicantDTO(Long applicationId, Long userId, String firstName, String lastName, String email,
			LocalDate appliedDate, String resumeFileName, byte[] resume, ApplicationStatus status) {
		super();
		this.applicationId = applicationId;
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.appliedDate = appliedDate;
		this.resumeFileName = resumeFileName;
		this.resume = resume;
		this.status = status;
	}



	public ApplicationStatus getStatus() {
		return status;
	}



	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}



	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Long applicationId) {
		this.applicationId = applicationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getAppliedDate() {
		return appliedDate;
	}

	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}

	public String getResumeFileName() {
		return resumeFileName;
	}

	public void setResumeFileName(String resumeFileName) {
		this.resumeFileName = resumeFileName;
	}

	public byte[] getResume() {
		return resume;
	}

	public void setResume(byte[] resume) {
		this.resume = resume;
	}
    
    
}
