package JobApplication.Controller;

//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import JobApplication.DTO.ApplicantDTO;
//import JobApplication.DTO.AppliedJobResponse;
//import JobApplication.DTO.SavedJobResponse;
//import JobApplication.DTO.UserDTO;
//import JobApplication.Entity.AppliedJob;
//import JobApplication.Entity.SavedJob;
//import JobApplication.FeignClients.UserClient;
//import JobApplication.Repository.AppliedJobRepository;
//import JobApplication.Service.JobApplicationService;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/job-application")
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
//@RequiredArgsConstructor
//public class JobApplicationController {
//
//	@Autowired
//    private  JobApplicationService service;
//	
//	@Autowired
//    private  UserClient userClient;
//	
//	@Autowired
//	private AppliedJobRepository appliedJobRepository;
//
//	@PostMapping("/apply")
//	public AppliedJob applyJob(
//	        @RequestParam Long userId,
//	        @RequestParam Long jobId,
//	        @RequestParam String jobTitle,
//	        @RequestParam String jobDescription,
//	        @RequestParam("resume") MultipartFile resumeFile
//	) {
//	    if (resumeFile == null || resumeFile.isEmpty()) {
//	        throw new RuntimeException("Resume file is required");
//	    }
//	    return service.applyJob(userId, jobId, jobTitle, jobDescription, resumeFile);
//	}
//
//
//    @PostMapping("/save")
//    public SavedJob saveJob(@RequestParam Long userId,
//    		                @RequestParam Long jobId) {
//        return service.saveJob(userId, jobId);
//    }
//
////    @GetMapping("/applied/{userId}")
////    public List<AppliedJob> getAppliedJobs(@PathVariable Long userId) {
////        return service.getAppliedJobs(userId);
////    }
////
////    @GetMapping("/saved/{userId}")
////    public List<SavedJob> getSavedJobs(@PathVariable Long userId) {
////        return service.getSavedJobs(userId);
////    }
//    
//    @GetMapping("/applied/{userId}")
//    public List<AppliedJobResponse> getAppliedJobss(@PathVariable Long userId) {
//        return service.getAppliedJobss(userId);
//    }
//
//    @GetMapping("/saved/{userId}")
//    public List<SavedJobResponse> getSavedJobss(@PathVariable Long userId) {
//        return service.getSavedJobss(userId);
//    }
//    
//    @GetMapping("/job/{jobId}/applicants")
//    public List<ApplicantDTO> getApplicantsForJob(@PathVariable Long jobId) {
//        List<AppliedJob> appliedJobs = appliedJobRepository.findByJobId(jobId);
//
//        return appliedJobs.stream().map(appliedJob -> {
//            UserDTO user = userClient.getUserDetails(appliedJob.getUserId());
//            return new ApplicantDTO(
//                appliedJob.getId(),
//                user.getId(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getEmail(),
//                appliedJob.getAppliedDate(),
//                appliedJob.getResumeFileName(),
//                appliedJob.getResume()
//            );
//        }).toList();
//    }
//    
//    @GetMapping("/download-resume/{appliedJobId}")
//    public ResponseEntity<byte[]> downloadResume(@PathVariable Long appliedJobId) {
//        AppliedJob appliedJob = appliedJobRepository.findById(appliedJobId)
//                .orElseThrow(() -> new RuntimeException("Application not found"));
//
//        return ResponseEntity.ok()
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + appliedJob.getResumeFileName() + "\"")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(appliedJob.getResume());
//    }
//
//    
//}




import JobApplication.DTO.ApplicantDTO;
import JobApplication.DTO.AppliedJobDTO;
import JobApplication.DTO.SavedJobDTO;
import JobApplication.Entity.AppliedJob;
import JobApplication.Entity.Notification;
import JobApplication.Entity.SavedJob;
import JobApplication.Service.JobApplicationService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/job-application")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class JobApplicationController {

	@Autowired
    private  JobApplicationService service;

    // apply - multipart form
    @PostMapping(value = "/apply", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AppliedJob applyJob(@RequestParam Long userId,
                               @RequestParam Long jobId,
                               @RequestParam String jobTitle,
                               @RequestParam String jobDescription,
                               @RequestPart(required = false) MultipartFile resume) {
        return service.applyJob(userId, jobId, jobTitle, jobDescription, resume);
    }

    @PostMapping("/save")
    public SavedJob saveJob(@RequestParam Long userId, @RequestParam Long jobId) {
        return service.saveJob(userId, jobId);
    }

    @GetMapping("/applied/{userId}")
    public List<AppliedJobDTO> getAppliedJobs(@PathVariable Long userId) {
        return service.getAppliedJobs(userId);
    }

    @GetMapping("/saved/{userId}")
    public List<SavedJobDTO> getSavedJobs(@PathVariable Long userId) {
        return service.getSavedJobs(userId);
    }

    @GetMapping("/job/{jobId}/applicants")
    public List<ApplicantDTO> getApplicantsForJob(@PathVariable Long jobId) {
        return service.getApplicantsForJob(jobId);
    }

    @GetMapping("/download-resume/{appliedJobId}")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long appliedJobId) {
        AppliedJob appliedJob = service.getAppliedJobById(appliedJobId);
        if (appliedJob.getResume() == null) {
            return ResponseEntity.badRequest().body(new byte[0]);
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + appliedJob.getResumeFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(appliedJob.getResume());
    }

   
    @PostMapping("/shortlist")
    public AppliedJob shortlist(@RequestParam Long appliedJobId,
                                @RequestParam(required = false) Long recruiterId) {
        return service.shortlistApplication(appliedJobId, recruiterId);
    }

   
    @PostMapping("/schedule-interview")
    public AppliedJob scheduleInterview(
            @RequestParam Long appliedJobId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime interviewDateTime,
            @RequestParam(required = false) String mode,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String meetingLink) {

        LocalDate date = interviewDateTime.toLocalDate();
        LocalTime time = interviewDateTime.toLocalTime();

        return service.scheduleInterview(appliedJobId, date, time, mode, location, meetingLink);
    }


   
    @PostMapping("/select")
    public AppliedJob selectApplicant(@RequestParam Long appliedJobId,
                                      @RequestParam(required = false) String offerDetails,
                                      @RequestParam(required = false) Long recruiterId) {
        return service.selectApplicant(appliedJobId, offerDetails, recruiterId);
    }

   
    @PostMapping("/reject")
    public AppliedJob rejectApplicant(@RequestParam Long appliedJobId,
                                      @RequestParam(required = false) Long recruiterId,
                                      @RequestParam(required = false) String reason) {
        return service.rejectApplicant(appliedJobId, recruiterId, reason);
    }

  
    @GetMapping("/notifications/user/{userId}")
    public List<Notification> notificationsForUser(@PathVariable Long userId) {
        return service.getNotificationsForUser(userId);
    }

    @GetMapping("/notifications/recruiter/{recruiterId}")
    public List<Notification> notificationsForRecruiter(@PathVariable Long recruiterId) {
        return service.getNotificationsForRecruiter(recruiterId);
    }
}

