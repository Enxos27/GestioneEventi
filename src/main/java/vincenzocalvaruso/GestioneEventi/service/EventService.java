package vincenzocalvaruso.GestioneEventi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vincenzocalvaruso.GestioneEventi.entity.Event;
import vincenzocalvaruso.GestioneEventi.entity.Role;
import vincenzocalvaruso.GestioneEventi.entity.User;
import vincenzocalvaruso.GestioneEventi.exceptions.BadRequestException;
import vincenzocalvaruso.GestioneEventi.exceptions.NotFoundException;
import vincenzocalvaruso.GestioneEventi.exceptions.UnauthorizedException;
import vincenzocalvaruso.GestioneEventi.payloads.EventDTO;
import vincenzocalvaruso.GestioneEventi.repository.EventRepository;
import vincenzocalvaruso.GestioneEventi.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    // Creo un nuovo evento
    public Event createEvent(EventDTO dto, UUID organizerId) {
        // 1. Recupero l'utente dal DB
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new NotFoundException("Utente non trovato"));

        // 2. Controllo Ruolo
        if (organizer.getRole() != Role.ORGANIZER) {
            throw new BadRequestException("Solo un Organizzatore di Eventi può compiere questa azione");
        }

        Event event = new Event();
        event.setTitle(dto.title());
        event.setDescription(dto.description());
        event.setEventDate(dto.eventDate());
        event.setLocation(dto.location());
        event.setMaxSeats(dto.maxSeats());
        event.setOrganizer(organizer);

        return eventRepository.save(event);
    }

    public Event updateEvent(UUID eventId, EventDTO body, User organizer) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        // CONTROLLO SICUREZZA: Sei tu l'organizzatore?
        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new UnauthorizedException("Non hai i permessi per modificare questo evento");
        }

        event.setTitle(body.title());
        event.setDescription(body.description());
        event.setEventDate(body.eventDate());
        event.setLocation(body.location());
        event.setMaxSeats(body.maxSeats());

        return eventRepository.save(event);
    }

    public void deleteEvent(UUID eventId, User organizer) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Evento non trovato"));

        // CONTROLLO SICUREZZA
        if (!event.getOrganizer().getId().equals(organizer.getId())) {
            throw new UnauthorizedException("Non hai i permessi per eliminare questo evento");
        }

        eventRepository.delete(event);
    }

    // Lista di tutti gli eventi (per gli utenti normali)
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }
}