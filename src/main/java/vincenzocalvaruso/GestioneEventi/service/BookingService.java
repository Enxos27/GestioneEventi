package vincenzocalvaruso.GestioneEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.GestioneEventi.entity.Booking;
import vincenzocalvaruso.GestioneEventi.entity.Event;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.exceptions.NotFoundException;
import vincenzocalvaruso.GestioneEventi.exceptions.UnauthorizedException;
import vincenzocalvaruso.GestioneEventi.repository.BookingRepository;
import vincenzocalvaruso.GestioneEventi.repository.EventRepository;
import vincenzocalvaruso.GestioneEventi.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private EventRepository eventRepo;
    @Autowired
    private UserRepository userRepo;

    public Booking createBooking(UUID eventId, User user) {
        // 1. Recupero l'evento
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        // 2. Controllo disponibilità posti
        long bookedSeats = bookingRepository.countByEventId(eventId);
        if (bookedSeats >= event.getMaxSeats()) {
            throw new RuntimeException("Sold out! Non ci sono più posti disponibili.");
        }

        // 3. Controllo se l'utente è già prenotato
        if (bookingRepository.existsByUserIdAndEventId(user.getId(), eventId)) {
            throw new RuntimeException("Ti sei già prenotato per questo evento!");
        }

        // 4. Creazione prenotazione
        Booking booking = new Booking();
        booking.setEvent(event);
        booking.setUser(user);
        booking.setBookingDate(LocalDateTime.now());

        return bookingRepository.save(booking);
    }

    public List<Booking> getUserBookings(UUID userId) {
        return bookingRepository.findByUserId(userId);
    }

    public void deleteBooking(UUID bookingId, User user) {
        // 1. Cerco la prenotazione
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Prenotazione non trovata"));

        // 2. CONTROLLO SICUREZZA: "L'utente che cancella è il proprietario della prenotazione?"
        if (!booking.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("Non puoi annullare una prenotazione non tua!");
        }

        // 3. Incremento +1 i posti disponbili
        Event event = booking.getEvent();
        event.setMaxSeats(event.getAvailableSeats() + 1);
        eventRepo.save(event);

        // 4. Elimino la prenotazione
        bookingRepository.delete(booking);
    }
}
