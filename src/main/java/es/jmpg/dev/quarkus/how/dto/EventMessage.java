package es.jmpg.dev.quarkus.how.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

/**
 * EventMessage is used for Kafka event streaming.
 * Unlike Spring Cloud Stream which often requires separate message models,
 * Quarkus allows direct JSON serialization with SmallRye Reactive Messaging.
 */
public class EventMessage {

    @JsonProperty("id")
    public Long id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("description")
    public String description;

    @JsonProperty("location")
    public String location;

    @JsonProperty("startDate")
    public LocalDateTime startDate;

    @JsonProperty("endDate")
    public LocalDateTime endDate;

    @JsonProperty("capacity")
    public Integer capacity;

    @JsonProperty("availableSeats")
    public Integer availableSeats;

    @JsonProperty("status")
    public String status;

    @JsonProperty("timestamp")
    public LocalDateTime timestamp;

    @JsonProperty("action")
    public String action;

    public EventMessage() {
    }

    public EventMessage(Long id, String title, String description, String location,
                        LocalDateTime startDate, LocalDateTime endDate, Integer capacity,
                        Integer availableSeats, String status, String action) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.capacity = capacity;
        this.availableSeats = availableSeats;
        this.status = status;
        this.action = action;
        this.timestamp = LocalDateTime.now();
    }
}
