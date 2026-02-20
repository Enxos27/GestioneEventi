package vincenzocalvaruso.GestioneEventi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime bookingDate; // Quando è avvenuta la prenotazione

    // RELAZIONE: Molte prenotazioni appartengono a un Utente
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // RELAZIONE: Molte prenotazioni appartengono a un Evento
    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    // Costruttore rapido per creare una prenotazione
    public Booking(User user, Event event) {
        this.user = user;
        this.event = event;
        this.bookingDate = LocalDateTime.now();
    }
}