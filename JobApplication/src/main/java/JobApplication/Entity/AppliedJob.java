package JobApplication.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import JobApplication.ApplicationStatus;

@Entity
@Table(name = "applied_jobs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AppliedJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long jobId;
    private Long userId;

    private LocalDate appliedDate;

    private boolean active = true;
    
    private String resumeFileName;// for possible withdraw later
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] resume;
    
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    private LocalDate interviewDate;       // Date of interview
    private LocalTime interviewTime;       // Time of interview
    private String interviewMode;          // ONLINE / OFFLINE
    private String interviewLocation;      // if offline
    private String meetingLink;            // if online
    private String interviewStatus;

    private boolean offerReleased = false;
    @Column(columnDefinition = "TEXT")
    private String offerDetails;

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
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public AppliedJob() {
		super();
		// TODO Auto-generated constructor stub
	}



	public byte[] getResume() {
		return resume;
	}

	public void setResume(byte[] resume) {
		this.resume = resume;
	}



	public AppliedJob(Long id, Long jobId, Long userId, LocalDate appliedDate, boolean active, String resumeFileName,
			byte[] resume, ApplicationStatus status, LocalDate interviewDate, LocalTime interviewTime,
			String interviewMode, String interviewLocation, String meetingLink, String interviewStatus,
			boolean offerReleased, String offerDetails) {
		super();
		this.id = id;
		this.jobId = jobId;
		this.userId = userId;
		this.appliedDate = appliedDate;
		this.active = active;
		this.resumeFileName = resumeFileName;
		this.resume = resume;
		this.status = status;
		this.interviewDate = interviewDate;
		this.interviewTime = interviewTime;
		this.interviewMode = interviewMode;
		this.interviewLocation = interviewLocation;
		this.meetingLink = meetingLink;
		this.interviewStatus = interviewStatus;
		this.offerReleased = offerReleased;
		this.offerDetails = offerDetails;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
		this.status = status;
	}

	

	public String getInterviewMode() {
		return interviewMode;
	}

	public void setInterviewMode(String interviewMode) {
		this.interviewMode = interviewMode;
	}



	public boolean isOfferReleased() {
		return offerReleased;
	}

	public void setOfferReleased(boolean offerReleased) {
		this.offerReleased = offerReleased;
	}

	public String getOfferDetails() {
		return offerDetails;
	}

	public void setOfferDetails(String offerDetails) {
		this.offerDetails = offerDetails;
	}

	public LocalDate getInterviewDate() {
		return interviewDate;
	}

	public void setInterviewDate(LocalDate interviewDate) {
		this.interviewDate = interviewDate;
	}

	public LocalTime getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(LocalTime interviewTime) {
		this.interviewTime = interviewTime;
	}

	public String getInterviewLocation() {
		return interviewLocation;
	}

	public void setInterviewLocation(String interviewLocation) {
		this.interviewLocation = interviewLocation;
	}

	public String getMeetingLink() {
		return meetingLink;
	}

	public void setMeetingLink(String meetingLink) {
		this.meetingLink = meetingLink;
	}

	public String getInterviewStatus() {
		return interviewStatus;
	}

	public void setInterviewStatus(String interviewStatus) {
		this.interviewStatus = interviewStatus;
	}


    
    
}

