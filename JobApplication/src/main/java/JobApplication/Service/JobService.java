package JobApplication.Service;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import JobApplication.DTO.RecruiterDTO;
import JobApplication.Entity.Job;
import JobApplication.FeignClients.RecruiterClient;
import JobApplication.Repository.JobRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

	@Autowired
    private  JobRepository jobRepository;
	
	@Autowired
    private  RecruiterClient recruiterClient;
	
	@Autowired
    private  EmailService emailService;

    // ✅ Post a job
    public Job postJob(Job job) {
        RecruiterDTO recruiter = recruiterClient.getRecruiterById(job.getRecruiterId());
        if (recruiter == null) throw new RuntimeException("Recruiter not found!");

        job.setPostedDate(LocalDate.now());
        job.setActive(true);

        Job saved = jobRepository.save(job);

        // Send email
        emailService.sendJobPostedEmail(
                recruiter.getEmail(),
                recruiter.getUsername(),
                job.getTitle(),
                recruiter.getCompanyName()
        );

        return saved;
    }

    // ✅ Get all jobs
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // ✅ Get jobs by recruiter
    public List<Job> getJobsByRecruiter(Long recruiterId) {
        return jobRepository.findByRecruiterId(recruiterId);
    }

    // ✅ Get expired jobs by recruiter
    public List<Job> getExpiredJobsByRecruiter(Long recruiterId) {
        return jobRepository.findByRecruiterIdAndActiveFalse(recruiterId);
    }

    // ✅ Search jobs
    public List<Job> searchJobs(String keyword) {
        return jobRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(
                keyword, keyword, keyword
        );
    }

    // ✅ Update job
    public Job updateJob(Long jobId, Job updatedJob) {
        Job existing = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        existing.setTitle(updatedJob.getTitle());
        existing.setDescription(updatedJob.getDescription());
        existing.setLocation(updatedJob.getLocation());
        existing.setType(updatedJob.getType());
        existing.setExpiryDate(updatedJob.getExpiryDate());
        return jobRepository.save(existing);
    }

    // ✅ Delete job
    public void deleteJob(Long jobId) {
        jobRepository.deleteById(jobId);
    }

    // ✅ Automatic expiry
    @Scheduled(cron = "0 0 0 * * ?") // runs every midnight
    public void expireJobs() {
        List<Job> jobs = jobRepository.findByActiveTrue();
        LocalDate today = LocalDate.now();
        jobs.forEach(job -> {
            if (job.getExpiryDate() != null && job.getExpiryDate().isBefore(today)) {
                job.setActive(false);
                jobRepository.save(job);
            }
        });
    }
}
    