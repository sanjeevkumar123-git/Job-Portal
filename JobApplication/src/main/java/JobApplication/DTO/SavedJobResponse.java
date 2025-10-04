package JobApplication.DTO;

import java.time.LocalDate;

public class SavedJobResponse {
    private Long jobId;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate savedDate;
	public SavedJobResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SavedJobResponse(Long jobId, String title, String description, String location, String type,
			LocalDate savedDate) {
		super();
		this.jobId = jobId;
		this.title = title;
		this.description = description;
		this.location = location;
		this.type = type;
		this.savedDate = savedDate;
	}
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public LocalDate getSavedDate() {
		return savedDate;
	}
	public void setSavedDate(LocalDate savedDate) {
		this.savedDate = savedDate;
	}

    // getters and setters
    
    
}