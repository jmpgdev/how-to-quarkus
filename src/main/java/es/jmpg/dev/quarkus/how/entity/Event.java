package es.jmpg.dev.quarkus.how.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Event entity uses Quarkus Panache ORM which extends Hibernate with active record pattern.
 * Panache provides static finder methods (findById, list, etc.) reducing boilerplate vs standard JPA,
 * and faster compile-time code generation. Spring Data JPA requires interfaces and more runtime reflection.
 */
@Entity
@Table(name = "events")
public class Event extends PanacheEntity {

    @NotBlank(message = "Event title is required")
    @Column(nullable = false)
    public String title;

    @Column(length = 1000)
    public String description;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    public String location;

    @NotNull(message = "Start date is required")
    @Column(nullable = false)
    public LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Column(nullable = false)
    public LocalDateTime endDate;

    @Column(nullable = false)
    public Integer capacity;

    @Column(nullable = false)
    public Integer availableSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public EventStatus status = EventStatus.SCHEDULED;

    @Column(nullable = false, updatable = false)
    public LocalDateTime createdAt;

    @Column(nullable = false)
    public LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (availableSeats == null && capacity != null) {
            availableSeats = capacity;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EventStatus {
        SCHEDULED,
        ONGOING,
        COMPLETED,
        CANCELLED
    }
}
