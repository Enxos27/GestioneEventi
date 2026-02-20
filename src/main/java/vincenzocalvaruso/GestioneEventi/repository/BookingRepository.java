package vincenzocalvaruso.GestioneEventi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzocalvaruso.GestioneEventi.entity.Booking;

import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    // Utile per l'utente che vuole vedere le sue prenotazioni (Extra)
    List<Booking> findByUserId(UUID userId);

    // Utile per controllare se un utente ha già prenotato un evento specifico
    boolean existsByUserIdAndEventId(UUID userId, UUID eventId);
}