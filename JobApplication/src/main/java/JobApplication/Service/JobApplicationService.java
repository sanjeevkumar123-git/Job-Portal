package JobApplication.Service;






import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import JobApplication.ApplicationStatus;
import JobApplication.DTO.ApplicantDTO;
import JobApplication.DTO.AppliedJobDTO;
import JobApplication.DTO.SavedJobDTO;
import JobApplication.DTO.UserDTO;
import JobApplication.Entity.AppliedJob;
import JobApplication.Entity.Job;
import JobApplication.Entity.Notification;
import JobApplication.Entity.SavedJob;
import JobApplication.FeignClients.UserClient;
import JobApplication.Repository.AppliedJobRepository;
import JobApplication.Repository.JobRepository;
import JobApplication.Repository.NotificationRepository;
import JobApplication.Repository.SavedJobRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobApplicationService {

    @Autowired
    private  AppliedJobRepository appliedJobRepository;

    @Autowired
    private  SavedJobRepository savedJobRepository;
    
    @Autowired
    private  JobRepository jobRepository;

    @Autowired
    private  NotificationRepository notificationRepository;

    @Autowired
    private  UserClient userClient;

    @Autowired
    private  JavaMailSender mailSender;

   
    @Transactional
    public AppliedJob applyJob(Long userId, Long jobId, String jobTitle, String jobDescription, MultipartFile resumeFile) {
       
        if (appliedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new RuntimeException("User already applied to this job");
        }

        AppliedJob applied = new AppliedJob();
        applied.setUserId(userId);
        applied.setJobId(jobId);
        applied.setAppliedDate(LocalDate.now());
        applied.setActive(true);
        applied.setStatus(ApplicationStatus.APPLIED);

        if (resumeFile != null && !resumeFile.isEmpty()) {
            try {
                applied.setResume(resumeFile.getBytes());
                applied.setResumeFileName(resumeFile.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read resume file");
            }
        }

        appliedJobRepository.save(applied);

     
        createNotificationForUser(userId, "Application Received",
                "Your application for job " + jobId + " has been received.");
        UserDTO user = userClient.getUserDetails(userId);
        sendSimpleEmail(user.getEmail(), "Application Received", "We received your application for " + jobTitle);

        return applied;
    }

  
    @Transactional
    public SavedJob saveJob(Long userId, Long jobId) {
        if (savedJobRepository.existsByUserIdAndJobId(userId, jobId)) {
            throw new RuntimeException("Job already saved");
        }
        SavedJob s = new SavedJob();
        s.setUserId(userId);
        s.setJobId(jobId);
        s.setSavedDate(LocalDate.now());
        savedJobRepository.save(s);
        createNotificationForUser(userId, "Job Saved", "You saved job " + jobId);
        return s;
    }

    public List<AppliedJobDTO> getAppliedJobs(Long userId) {
        List<AppliedJob> appliedJobs = appliedJobRepository.findByUserId(userId);

        return appliedJobs.stream().map(applied -> {
            Job job = jobRepository.findById(applied.getJobId()).orElse(null);
            AppliedJobDTO dto = new AppliedJobDTO();
            dto.setId(applied.getId());
            dto.setAppliedDate(applied.getAppliedDate());
            dto.setStatus(applied.getStatus());
            dto.setResumeFileName(applied.getResumeFileName());
            dto.setJob(job);
            dto.setStatus(applied.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<SavedJobDTO> getSavedJobs(Long userId) {
        List<SavedJob> savedJobs = savedJobRepository.findByUserId(userId);

        return savedJobs.stream().map(saved -> {
            Job job = jobRepository.findById(saved.getJobId()).orElse(null);
            SavedJobDTO dto = new SavedJobDTO();
            dto.setId(saved.getId());
            dto.setSavedDate(saved.getSavedDate());
            dto.setJob(job);
            return dto;
        }).collect(Collectors.toList());
    }

    
    public List<ApplicantDTO> getApplicantsForJob(Long jobId) {
        List<AppliedJob> list = appliedJobRepository.findByJobId(jobId);
        return list.stream().map(appliedJob -> {
            UserDTO user = userClient.getUserDetails(appliedJob.getUserId());
            return new ApplicantDTO(
                    appliedJob.getId(),
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    appliedJob.getAppliedDate(),
                    appliedJob.getResumeFileName(),
                    appliedJob.getResume(),
                    appliedJob.getStatus()
            );
        }).collect(Collectors.toList());
    }

  
    public AppliedJob getAppliedJobById(Long appliedJobId) {
        return appliedJobRepository.findById(appliedJobId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
    }


    @Transactional
    public AppliedJob shortlistApplication(Long appliedJobId, Long recruiterId) {
        AppliedJob applied = getAppliedJobById(appliedJobId);

        if (applied.getStatus() != ApplicationStatus.APPLIED) {
            throw new IllegalStateException("Application is already processed. Cannot shortlist again.");
        }

        applied.setStatus(ApplicationStatus.SHORTLISTED);
        appliedJobRepository.save(applied);

        Long userId = applied.getUserId();
        createNotificationForUser(userId, "Application Shortlisted",
                "Your application for job " + applied.getJobId() + " was shortlisted.");
        UserDTO user = userClient.getUserDetails(userId);
        sendSimpleEmail(user.getEmail(), "Application Shortlisted",
                "You have been shortlisted for job " + applied.getJobId());
        return applied;
    }

 
    @Transactional
    public AppliedJob scheduleInterview(Long appliedJobId,
                                        LocalDate interviewDate,
                                        LocalTime interviewTime,
                                        String mode,
                                        String location,
                                        String meetingLink) {
        AppliedJob applied = getAppliedJobById(appliedJobId);

        if (applied.getStatus() != ApplicationStatus.SHORTLISTED) {
            throw new IllegalStateException("Interview can only be scheduled after shortlisting.");
        }

        applied.setStatus(ApplicationStatus.INTERVIEW_SCHEDULED);
        applied.setInterviewDate(interviewDate);
        applied.setInterviewTime(interviewTime);
        applied.setInterviewMode(mode);
        applied.setInterviewLocation(location);
        applied.setMeetingLink(meetingLink);
        applied.setInterviewStatus("SCHEDULED");

        appliedJobRepository.save(applied);

        Long userId = applied.getUserId();
        createNotificationForUser(userId, "Interview Scheduled",
                "Your interview for job " + applied.getJobId()
                + " is scheduled on " + interviewDate + " at " + interviewTime);
        UserDTO user = userClient.getUserDetails(userId);
        sendSimpleEmail(user.getEmail(), "Interview Scheduled",
                "Interview scheduled on " + interviewDate + " at " + interviewTime
                + (meetingLink != null ? " | Link: " + meetingLink : "")
                + (location != null ? " | Location: " + location : ""));

        return applied;
    }


  
    @Transactional
    public AppliedJob selectApplicant(Long appliedJobId, String offerDetails, Long recruiterId) {
        AppliedJob applied = getAppliedJobById(appliedJobId);

        if (applied.getStatus() == ApplicationStatus.SELECTED || applied.getStatus() == ApplicationStatus.REJECTED) {
            throw new IllegalStateException("Applicant is already finalized (selected/rejected).");
        }

        applied.setStatus(ApplicationStatus.SELECTED);
        applied.setOfferReleased(true);
        applied.setOfferDetails(offerDetails);
        appliedJobRepository.save(applied);

        Long userId = applied.getUserId();
        createNotificationForUser(userId, "Offer Released",
                "Offer for job " + applied.getJobId() + " released. " + (offerDetails == null ? "" : offerDetails));
        UserDTO user = userClient.getUserDetails(userId);
        sendSimpleEmail(user.getEmail(), "Offer Released",
                "Congratulations! Offer: " + (offerDetails == null ? "See details in portal" : offerDetails));
        return applied;
    }

    @Transactional
    public AppliedJob rejectApplicant(Long appliedJobId, Long recruiterId, String reason) {
        AppliedJob applied = getAppliedJobById(appliedJobId);

        if (applied.getStatus() == ApplicationStatus.REJECTED || applied.getStatus() == ApplicationStatus.SELECTED) {
            throw new IllegalStateException("Applicant is already finalized (selected/rejected).");
        }

        applied.setStatus(ApplicationStatus.REJECTED);
        appliedJobRepository.save(applied);

        Long userId = applied.getUserId();
        createNotificationForUser(userId, "Application Update",
                "Your application for job " + applied.getJobId() + " was not selected. " + (reason == null ? "" : reason));
        UserDTO user = userClient.getUserDetails(userId);
        sendSimpleEmail(user.getEmail(), "Application Update",
                "Sorry — your application was not selected. " + (reason == null ? "" : reason));
        return applied;
    }

   
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }
    public List<Notification> getNotificationsForRecruiter(Long recruiterId) {
        return notificationRepository.findByRecruiterIdOrderByCreatedAtDesc(recruiterId);
    }
    public void createNotificationForUser(Long userId, String title, String message) {
        Notification n = new Notification(userId, null, title, message);
        notificationRepository.save(n);
    }
    public void createNotificationForRecruiter(Long recruiterId, String title, String message) {
        Notification n = new Notification(null, recruiterId, title, message);
        notificationRepository.save(n);
    }

    
    private void sendSimpleEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            
            System.err.println("Failed to send email to " + to + " : " + e.getMessage());
        }
    }
    
    
}
