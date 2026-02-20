package vincenzocalvaruso.GestioneEventi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vincenzocalvaruso.GestioneEventi.entity.Event;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    // Utile per l'organizzatore che vuole vedere solo i propri eventi
    List<Event> findByOrganizerId(UUID organizerId);
}