package JobApplication.DTO;

import java.time.LocalDate;

import JobApplication.ApplicationStatus;
import JobApplication.Entity.Job;

public class AppliedJobDTO {
    private Long id;
    private LocalDate appliedDate;
    private ApplicationStatus status;
    private String resumeFileName;
    private Job job; // full job details
	public AppliedJobDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppliedJobDTO(Long id, LocalDate appliedDate, ApplicationStatus status, String resumeFileName, Job job) {
		super();
		this.id = id;
		this.appliedDate = appliedDate;
		this.status = status;
		this.resumeFileName = resumeFileName;
		this.job = job;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getAppliedDate() {
		return appliedDate;
	}
	public void setAppliedDate(LocalDate appliedDate) {
		this.appliedDate = appliedDate;
	}
	public ApplicationStatus getStatus() {
		return status;
	}
	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}
	public String getResumeFileName() {
		return resumeFileName;
	}
	public void setResumeFileName(String resumeFileName) {
		this.resumeFileName = resumeFileName;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
    
    
    
}