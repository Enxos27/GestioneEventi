package vincenzocalvaruso.GestioneEventi.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "organizer", cascade = CascadeType.ALL)
    private List<Event> createdEvents;

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.USER; // Default a USER se null
    }
}
