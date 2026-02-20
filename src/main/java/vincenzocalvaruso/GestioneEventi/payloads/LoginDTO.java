package vincenzocalvaruso.GestioneEventi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @Email(message = "L'email inserita non è valida")
        @NotBlank(message = "L'email è obbligatoria")
        String email,
        @NotBlank
        String password
) {
}
