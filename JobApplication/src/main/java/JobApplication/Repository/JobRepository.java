package JobApplication.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import JobApplication.Entity.Job;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByRecruiterId(Long recruiterId);
    List<Job> findByActiveTrue();
    List<Job> findByRecruiterIdAndActiveFalse(Long recruiterId);
    List<Job> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(
            String title, String desc, String location
    );
}


