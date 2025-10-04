package JobApplication.FeignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import JobApplication.DTO.RecruiterDTO;

@FeignClient(name = "Job" , url = "${Recruiter-service.url}" )

public interface RecruiterClient {
    @GetMapping("/api/recruiter/auth/{recruiterId}/profile")
    RecruiterDTO getRecruiterById(@PathVariable Long recruiterId);
}

