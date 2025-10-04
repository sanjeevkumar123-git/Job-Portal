package recruiter.Job.DTO;



public class LoginResponseDTO {
    private String token;
    private String username;
    private String email;

    public LoginResponseDTO(String token, String username, String email) {
        this.token = token;
        this.username = username;
        this.email = email;
    }

    // getters
    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
}
