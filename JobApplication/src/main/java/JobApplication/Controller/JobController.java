package JobApplication.Controller;


import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import JobApplication.Entity.Job;
import JobApplication.Service.JobService;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RequiredArgsConstructor
public class JobController {

	
	@Autowired
    private JobService jobService;

    @PostMapping("/post")
    public ResponseEntity<Job> postJob(@RequestBody Job job) {
        return ResponseEntity.ok(jobService.postJob(job));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @GetMapping("/recruiter/{recruiterId}")
    public ResponseEntity<List<Job>> getJobsByRecruiter(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(jobService.getJobsByRecruiter(recruiterId));
    }

    @GetMapping("/recruiter/{recruiterId}/expired")
    public ResponseEntity<List<Job>> getExpiredJobsByRecruiter(@PathVariable Long recruiterId) {
        return ResponseEntity.ok(jobService.getExpiredJobsByRecruiter(recruiterId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(@RequestParam String keyword) {
        return ResponseEntity.ok(jobService.searchJobs(keyword));
    }

    @PutMapping("/{jobId}")
    public ResponseEntity<Job> updateJob(@PathVariable Long jobId, @RequestBody Job updatedJob) {
        return ResponseEntity.ok(jobService.updateJob(jobId, updatedJob));
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }
}
