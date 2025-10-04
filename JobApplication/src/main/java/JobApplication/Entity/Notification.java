package JobApplication.Entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;         // jobseeker recipient
    private Long recruiterId;    // recruiter recipient

    private String title;
    @Column(columnDefinition = "TEXT")
    private String message;

    private boolean readNotification = false;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Notification() {}

    public Notification(Long userId, Long recruiterId, String title, String message) {
        this.userId = userId;
        this.recruiterId = recruiterId;
        this.title = title;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }

    // getters/setters...
    public Long getId(){return id;}
    public void setId(Long id){this.id=id;}
    public Long getUserId(){return userId;}
    public void setUserId(Long userId){this.userId=userId;}
    public Long getRecruiterId(){return recruiterId;}
    public void setRecruiterId(Long recruiterId){this.recruiterId=recruiterId;}
    public String getTitle(){return title;}
    public void setTitle(String title){this.title=title;}
    public String getMessage(){return message;}
    public void setMessage(String message){this.message=message;}
    public boolean isReadNotification(){return readNotification;}
    public void setReadNotification(boolean readNotification){this.readNotification=readNotification;}
    public LocalDateTime getCreatedAt(){return createdAt;}
    public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}

