package vincenzocalvaruso.GestioneEventi.payloads;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponseDTO(
        UUID id,
        String title,
        String description,
        LocalDateTime eventDate,
        String location,
        int maxSeats,
        int availableSeats,
        String organizerEmail // Metto solo l'email invece di tutto l'oggetto User
) {
}