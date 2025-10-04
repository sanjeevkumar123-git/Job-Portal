package User.DTO;

import java.time.LocalDate;

public class ResumeDTO {
	 
    private Long id;
    private String resumeName;
    private LocalDate uploadDate;

    
    
    
    
	public String getResumeName() {
		return resumeName;
	}
	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}
    // Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(LocalDate uploadDate) {
		this.uploadDate = uploadDate;
	}
    
    
    
}
