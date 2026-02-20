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

    // Endpoint per vedere tutti gli eventi
    @GetMapping
    public ResponseEntity<List<Event>> list() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }
}