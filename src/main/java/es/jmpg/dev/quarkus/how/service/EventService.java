package es.jmpg.dev.quarkus.how.service;

import es.jmpg.dev.quarkus.how.dto.EventDTO;
import es.jmpg.dev.quarkus.how.dto.EventResponseDTO;
import es.jmpg.dev.quarkus.how.entity.Event;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * EventService demonstrates Quarkus CDI dependency injection with @ApplicationScoped.
 * Unlike Spring's @Service + @Autowired pattern, Quarkus uses standard jakarta.inject providing
 * faster startup time and minimal reflection overhead, crucial for cloud-native deployments.
 */
@ApplicationScoped
public class EventService {

    public List<EventResponseDTO> getAllEvents() {
        return Event.<Event>listAll()
                .stream()
                .map(EventResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EventResponseDTO getEventById(Long id) {
        Event event = Event.findById(id);
        if (event == null) {
            throw new NotFoundException("Event with id " + id + " not found");
        }
        return EventResponseDTO.fromEntity(event);
    }

    @Transactional
    public EventResponseDTO createEvent(EventDTO eventDTO) {
        validateEventDates(eventDTO.startDate, eventDTO.endDate);

        Event event = eventDTO.toEntity();
        event.persist();
        
        return EventResponseDTO.fromEntity(event);
    }

    @Transactional
    public EventResponseDTO updateEvent(Long id, EventDTO eventDTO) {
        Event event = Event.findById(id);
        if (event == null) {
            throw new NotFoundException("Event with id " + id + " not found");
        }

        validateEventDates(eventDTO.startDate, eventDTO.endDate);

        event.title = eventDTO.title;
        event.description = eventDTO.description;
        event.location = eventDTO.location;
        event.startDate = eventDTO.startDate;
        event.endDate = eventDTO.endDate;

        if (eventDTO.capacity != null && !eventDTO.capacity.equals(event.capacity)) {
            int diff = eventDTO.capacity - event.capacity;
            event.capacity = eventDTO.capacity;
            event.availableSeats = event.availableSeats + diff;
        }

        event.persist();
        
        return EventResponseDTO.fromEntity(event);
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = Event.findById(id);
        if (event == null) {
            throw new NotFoundException("Event with id " + id + " not found");
        }
        event.delete();
    }

    @Transactional
    public EventResponseDTO updateEventStatus(Long id, Event.EventStatus status) {
        Event event = Event.findById(id);
        if (event == null) {
            throw new NotFoundException("Event with id " + id + " not found");
        }
        event.status = status;
        event.persist();
        return EventResponseDTO.fromEntity(event);
    }

    public List<EventResponseDTO> getEventsByStatus(Event.EventStatus status) {
        return Event.<Event>list("status", status)
                .stream()
                .map(EventResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private void validateEventDates(java.time.LocalDateTime startDate, java.time.LocalDateTime endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date");
        }
    }
}
