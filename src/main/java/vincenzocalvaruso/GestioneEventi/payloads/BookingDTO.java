package vincenzocalvaruso.GestioneEventi.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BookingDTO(
        @NotNull(message = "L'ID dell'evento è obbligatorio")
        UUID eventId
) {
}