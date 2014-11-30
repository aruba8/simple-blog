package blog.models;


import javax.persistence.*;

@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "sessions_string")
    private String sessionString;

    public String getSessionString() {
        return sessionString;
    }
    public void setSessionString(String sessionString) {
        this.sessionString = sessionString;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public long getId() {
        return id;
    }
}
