package User.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import User.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

	

	Optional<User> findByEmail(String email);



	User findByOtp(String otp);
}
