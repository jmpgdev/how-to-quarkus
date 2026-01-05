package es.jmpg.dev.quarkus.how.dto;

import es.jmpg.dev.quarkus.how.entity.Event;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class EventDTO {

    @NotBlank(message = "Event title is required")
    public String title;

    public String description;

    @NotBlank(message = "Location is required")
    public String location;

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    public LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    public LocalDateTime endDate;

    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    public Integer capacity;

    public Event toEntity() {
        Event event = new Event();
        event.title = this.title;
        event.description = this.description;
        event.location = this.location;
        event.startDate = this.startDate;
        event.endDate = this.endDate;
        event.capacity = this.capacity;
        event.availableSeats = this.capacity;
        return event;
    }

    public static EventDTO fromEntity(Event event) {
        EventDTO dto = new EventDTO();
        dto.title = event.title;
        dto.description = event.description;
        dto.location = event.location;
        dto.startDate = event.startDate;
        dto.endDate = event.endDate;
        dto.capacity = event.capacity;
        return dto;
    }
}
