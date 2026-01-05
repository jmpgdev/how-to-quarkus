package es.jmpg.dev.quarkus.how.dto;

import es.jmpg.dev.quarkus.how.entity.Event;

import java.time.LocalDateTime;

public class EventResponseDTO {

    public Long id;
    public String title;
    public String description;
    public String location;
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public Integer capacity;
    public Integer availableSeats;
    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    public static EventResponseDTO fromEntity(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.id = event.id;
        dto.title = event.title;
        dto.description = event.description;
        dto.location = event.location;
        dto.startDate = event.startDate;
        dto.endDate = event.endDate;
        dto.capacity = event.capacity;
        dto.availableSeats = event.availableSeats;
        dto.status = event.status.name();
        dto.createdAt = event.createdAt;
        dto.updatedAt = event.updatedAt;
        return dto;
    }
}
