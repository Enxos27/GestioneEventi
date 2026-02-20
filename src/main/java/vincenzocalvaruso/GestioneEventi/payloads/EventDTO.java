package vincenzocalvaruso.GestioneEventi.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record EventDTO(
        @NotBlank(message = "Il titolo è obbligatorio")
        String title,

        @Size(max = 1000)
        String description,

        @NotNull(message = "La data è obbligatoria")
        LocalDateTime eventDate,

        @NotBlank(message = "Il luogo è obbligatorio")
        String location,

        @Min(value = 1, message = "Deve esserci almeno un posto disponibile")
        int maxSeats
) {
}