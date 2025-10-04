package JobApplication.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import JobApplication.Entity.AppliedJob;

import java.util.List;
import java.util.Optional;

public interface AppliedJobRepository extends JpaRepository<AppliedJob, Long> {
    List<AppliedJob> findByUserId(Long userId);
 // AppliedJobRepository
    boolean existsByUserIdAndJobId(Long userId, Long jobId);
    List<AppliedJob> findByJobId(Long jobId);
    Optional<AppliedJob> findById(Long id);
}
