package es.jmpg.dev.quarkus.how.resource;

import es.jmpg.dev.quarkus.how.dto.EventDTO;
import es.jmpg.dev.quarkus.how.dto.EventResponseDTO;
import es.jmpg.dev.quarkus.how.entity.Event;
import es.jmpg.dev.quarkus.how.service.EventService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

/**
 * EventResource uses JAX-RS annotations (@Path, @GET, @POST, etc.) which are Jakarta EE standards.
 * Unlike Spring Web's @RestController/@RequestMapping, JAX-RS is portable across containers
 * and compiles to native code efficiently. OpenAPI annotations are also standardized via MicroProfile.
 */
@Path("/api/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Event Management", description = "Operations for managing events")
public class EventResource {

    @Inject
    EventService eventService;

    @GET
    @Operation(summary = "Get all events", description = "Retrieves a list of all events")
    @APIResponse(
            responseCode = "200",
            description = "List of events retrieved successfully",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class))
    )
    public Response getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();
        return Response.ok(events).build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get event by ID", description = "Retrieves a specific event by its ID")
    @APIResponse(
            responseCode = "200",
            description = "Event retrieved successfully",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class))
    )
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response getEventById(
            @Parameter(description = "Event ID", required = true)
            @PathParam("id") Long id) {
        EventResponseDTO event = eventService.getEventById(id);
        return Response.ok(event).build();
    }

    @POST
    @Operation(summary = "Create new event", description = "Creates a new event")
    @APIResponse(
            responseCode = "201",
            description = "Event created successfully",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response createEvent(@Valid EventDTO eventDTO) {
        EventResponseDTO created = eventService.createEvent(eventDTO);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update event", description = "Updates an existing event")
    @APIResponse(
            responseCode = "200",
            description = "Event updated successfully",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class))
    )
    @APIResponse(responseCode = "404", description = "Event not found")
    @APIResponse(responseCode = "400", description = "Invalid input data")
    public Response updateEvent(
            @Parameter(description = "Event ID", required = true)
            @PathParam("id") Long id,
            @Valid EventDTO eventDTO) {
        EventResponseDTO updated = eventService.updateEvent(id, eventDTO);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete event", description = "Deletes an event")
    @APIResponse(responseCode = "204", description = "Event deleted successfully")
    @APIResponse(responseCode = "404", description = "Event not found")
    public Response deleteEvent(
            @Parameter(description = "Event ID", required = true)
            @PathParam("id") Long id) {
        eventService.deleteEvent(id);
        return Response.noContent().build();
    }

    @PATCH
    @Path("/{id}/status")
    @Operation(summary = "Update event status", description = "Updates the status of an event")
    @APIResponse(
            responseCode = "200",
            description = "Event status updated successfully",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class))
    )
    @APIResponse(responseCode = "404", description = "Event not found")
    @APIResponse(responseCode = "400", description = "Invalid status")
    public Response updateEventStatus(
            @Parameter(description = "Event ID", required = true)
            @PathParam("id") Long id,
            @Parameter(description = "New status", required = true)
            @QueryParam("status") String status) {
        Event.EventStatus eventStatus = Event.EventStatus.valueOf(status.toUpperCase());
        EventResponseDTO updated = eventService.updateEventStatus(id, eventStatus);
        return Response.ok(updated).build();
    }

    @GET
    @Path("/status/{status}")
    @Operation(summary = "Get events by status", description = "Retrieves all events with a specific status")
    @APIResponse(
            responseCode = "200",
            description = "Events retrieved successfully",
            content = @Content(schema = @Schema(implementation = EventResponseDTO.class))
    )
    public Response getEventsByStatus(
            @Parameter(description = "Event status", required = true)
            @PathParam("status") String status) {
        Event.EventStatus eventStatus = Event.EventStatus.valueOf(status.toUpperCase());
        List<EventResponseDTO> events = eventService.getEventsByStatus(eventStatus);
        return Response.ok(events).build();
    }
}
