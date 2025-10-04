package JobApplication.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors()
            .and()
            .authorizeHttpRequests()
            
            // Jobs endpoints
            .requestMatchers("/api/jobs/post").permitAll()
            .requestMatchers("/api/jobs/all").permitAll()
            .requestMatchers("/api/jobs/recruiter/**").permitAll()
            .requestMatchers("/api/jobs/search").permitAll()
            .requestMatchers("/api/jobs/*").permitAll()

            // Job application endpoints
            .requestMatchers("/api/job-application/apply").permitAll()
            .requestMatchers("/api/job-application/save").permitAll()
            .requestMatchers("/api/job-application/applied/*").permitAll() // userId path variable
            .requestMatchers("/api/job-application/saved/*").permitAll()   // userId path variable
            .requestMatchers("/api/job-application/job/*/applicants").permitAll()
            .requestMatchers("/api/job-application/download-resume/*").permitAll()
            .requestMatchers("/api/job-application/shortlist").permitAll()
            .requestMatchers("/api/job-application/select").permitAll()
            .requestMatchers("/api/job-application/schedule-interview").permitAll()
            .requestMatchers("/api/job-application/reject").permitAll()

            // Notifications
            .requestMatchers("/api/job-application/notifications/user/*").permitAll()
            .requestMatchers("/api/job-application/notifications/recruiter/*").permitAll()

            // Any other request
            .anyRequest().authenticated();

        return http.build();
    }

    // CORS configuration
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // frontend origin
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
