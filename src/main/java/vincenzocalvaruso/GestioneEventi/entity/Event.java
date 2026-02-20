package vincenzocalvaruso.GestioneEventi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000) // Per descrizioni un po' più lunghe
    private String description;

    @Column(nullable = false)
    private LocalDateTime eventDate; // Usa LocalDateTime per gestire data e ora

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int maxSeats;

    // RELAZIONE: Molti eventi appartengono a un solo Organizzatore
    @ManyToOne
    @JoinColumn(name = "organizer_id", nullable = false)
    @JsonIgnore
    private User organizer;

    // RELAZIONE EXTRA: Per gestire le prenotazioni (Many-to-Many via Booking)
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("organizer")
    private List<Booking> bookings = new ArrayList<>();

    // Metodo helper per calcolare i posti rimanenti
    public int getAvailableSeats() {
        return this.maxSeats - this.bookings.size();
    }
}