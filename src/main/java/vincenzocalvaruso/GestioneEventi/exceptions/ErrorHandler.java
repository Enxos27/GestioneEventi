package vincenzocalvaruso.GestioneEventi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import vincenzocalvaruso.GestioneEventi.payloads.ErrorDTO;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(BadRequestException.class)
    // Tra le parentesi specifico il tipo di eccezione gestita da questo metodo
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    public ErrorDTO handleBadRequest(BadRequestException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 401
    public ErrorDTO handleUnauthorized(UnauthorizedException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403
    public ErrorDTO handleForbidden(AuthorizationDeniedException ex) {
        return new ErrorDTO("Non hai i permessi per accedere!", LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    public ErrorDTO handleNotFound(NotFoundException ex) {
        return new ErrorDTO(ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 500
    public ErrorDTO handleGenericServerError(Exception ex) {
        ex.printStackTrace();
        return new ErrorDTO("C'è stato un errore interno, contattare l'assistenza!", LocalDateTime.now());

    }


}