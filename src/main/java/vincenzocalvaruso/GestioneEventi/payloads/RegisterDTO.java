package vincenzocalvaruso.GestioneEventi.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import vincenzocalvaruso.GestioneEventi.entity.Role;

public record RegisterDTO(
        @Email(message = "L'email inserita non è valida")
        @NotBlank(message = "L'email è obbligatoria")
        String email,
        @NotBlank
        @Size(min = 4, message = "La password deve contenere più caratteri!")
        String password,
        Role role


) {
}