package User.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import User.Entity.Resume;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
	 List<Resume> findByUserId(Long userId);
}
