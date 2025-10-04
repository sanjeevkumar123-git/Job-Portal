package JobApplication.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "saved_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SavedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private Long userId;

    private LocalDate savedDate;

	public SavedJob(Long id, Long jobId, Long userId, LocalDate savedDate) {
		super();
		this.id = id;
		this.jobId = jobId;
		this.userId = userId;
		this.savedDate = savedDate;
	}

	public SavedJob() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDate getSavedDate() {
		return savedDate;
	}

	public void setSavedDate(LocalDate savedDate) {
		this.savedDate = savedDate;
	}
    
    
}
