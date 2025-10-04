package User.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resumeName;
     // Indicates a large object (LOB) data type
    
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private byte[] data; // Byte array to store resume content
    private LocalDate uploadDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResumeName() {
		return resumeName;
	}

	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public LocalDate getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDate uploadDate) {
		this.uploadDate = uploadDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Resume() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Resume(Long id, String resumeName, String filePath, LocalDate uploadDate, User user) {
		super();
		this.id = id;
		this.resumeName = resumeName;
		this.resumeName = filePath;
		this.uploadDate = uploadDate;
		this.user = user;
	}

    // Getters and Setters
    
    
}

