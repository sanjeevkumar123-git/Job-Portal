package recruiter.Job.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import recruiter.Job.Entity.Recruiter;

import java.util.Optional;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByUsername(String username);
    Optional<Recruiter> findByEmail(String email);
    Recruiter findByOtp(String otp);
}

