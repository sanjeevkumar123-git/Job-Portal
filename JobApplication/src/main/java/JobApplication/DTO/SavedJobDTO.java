package JobApplication.DTO;

import java.time.LocalDate;

import JobApplication.Entity.Job;

public class SavedJobDTO {
    private Long id;
    private LocalDate savedDate;
    private Job job; // full job details
	public SavedJobDTO(Long id, LocalDate savedDate, Job job) {
		super();
		this.id = id;
		this.savedDate = savedDate;
		this.job = job;
	}
	public SavedJobDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getSavedDate() {
		return savedDate;
	}
	public void setSavedDate(LocalDate savedDate) {
		this.savedDate = savedDate;
	}
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
    
    
    
}