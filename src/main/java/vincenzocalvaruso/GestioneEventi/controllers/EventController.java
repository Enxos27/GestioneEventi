package vincenzocalvaruso.GestioneEventi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vincenzocalvaruso.GestioneEventi.entity.Event;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.exceptions.ValidationException;
import vincenzocalvaruso.GestioneEventi.payloads.EventDTO;
import vincenzocalvaruso.GestioneEventi.service.EventService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasAnyAuthority('ORGANIZER')")
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@RequestBody @Validated EventDTO body, BindingResult validationResult, @AuthenticationPrincipal User currentOrganizer) {
        if (validationResult.hasErrors()) {
            List<String> errorsList = validationResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }

        return eventService.createEvent(body, currentOrganizer.getId());
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    public Event update(@PathVariable UUID eventId, @RequestBody EventDTO body, @AuthenticationPrincipal User organizer) {
        return eventService.updateEvent(eventId, body, organizer);
    }

    // 3. DELETE - Eliminare un evento (Solo l'organizzatore proprietario)
    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasAuthority('ORGANIZER')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Restituisce 204 se va a buon fine
    public void delete(@PathVariable UUID eventId,
                       @AuthenticationPrincipal User organizer) {
        eventService.deleteEvent(eventId, organizer);
    }

    // Endpoint per vedere tutti gli eventi
    @GetMapping
    public ResponseEntity<List<Event>> list() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}