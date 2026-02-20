package vincenzocalvaruso.GestioneEventi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import vincenzocalvaruso.GestioneEventi.entity.Booking;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.service.BookingService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasAuthority('USER')") // Solo gli utenti semplici possono prenotarsi
    public ResponseEntity<Booking> bookEvent(@PathVariable UUID eventId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(bookingService.createBooking(eventId, user));
    }

    @GetMapping("/my-bookings")
    @PreAuthorize("hasAuthority('USER')")
    public List<Booking> getMyBookings(@AuthenticationPrincipal User user) {
        // Estraggo l'ID dall'utente autenticato e cosi da poter chiedere al service le sue prenotazioni
        return bookingService.getUserBookings(user.getId());
    }

    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasAuthority('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Restituisce 204 se va a buon fine
    public void cancelBooking(@PathVariable UUID bookingId, @AuthenticationPrincipal User user) {
        bookingService.deleteBooking(bookingId, user);
    }
}