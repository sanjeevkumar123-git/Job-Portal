package recruiter.Job.ServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import recruiter.Job.Entity.Recruiter;
import recruiter.Job.Repository.RecruiterRepository;

@Service
public class RecruiterDetailsService implements UserDetailsService {

    @Autowired
    private RecruiterRepository recruiterRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Recruiter r = recruiterRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Recruiter not found"));
        // Only allow authenticated user objects for verified recruiters in auth logic
        return org.springframework.security.core.userdetails.User
                .withUsername(r.getUsername())
                .password(r.getPassword())
                .authorities("RECRUITER")
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!r.isVerified()) // unverified => disabled (optional)
                .build();
    }
}

