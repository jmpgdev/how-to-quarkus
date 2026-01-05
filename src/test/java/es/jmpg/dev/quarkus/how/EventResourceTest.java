package es.jmpg.dev.quarkus.how;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;

@QuarkusTest
public class EventResourceTest {

    @Test
    public void testGetAllEvents() {
        given()
                .when().get("/api/events")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$.size()", is(greaterThan(0)));
    }

    @Test
    public void testGetEventById() {
        given()
                .when().get("/api/events/1")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", is(1))
                .body("title", notNullValue());
    }

    @Test
    public void testGetEventByIdNotFound() {
        given()
                .when().get("/api/events/999")
                .then()
                .statusCode(404);
    }

    @Test
    public void testCreateEvent() {
        String eventJson = """
                {
                    "title": "Test Event",
                    "description": "Test Description",
                    "location": "Test Location",
                    "startDate": "2026-12-25T10:00:00",
                    "endDate": "2026-12-25T18:00:00",
                    "capacity": 100
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(eventJson)
                .when().post("/api/events")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("title", is("Test Event"))
                .body("capacity", is(100))
                .body("availableSeats", is(100));
    }

    @Test
    public void testCreateEventValidationError() {
        String invalidEventJson = """
                {
                    "title": "",
                    "location": "Test Location"
                }
                """;

        given()
                .contentType(ContentType.JSON)
                .body(invalidEventJson)
                .when().post("/api/events")
                .then()
                .statusCode(400);
    }

    @Test
    public void testUpdateEventStatus() {
        given()
                .queryParam("status", "ONGOING")
                .when().patch("/api/events/1/status")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("status", is("ONGOING"));
    }

    @Test
    public void testGetEventsByStatus() {
        given()
                .when().get("/api/events/status/SCHEDULED")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("$.size()", is(greaterThan(0)));
    }

    @Test
    public void testDeleteEvent() {
        // First create an event to delete
        String eventJson = """
                {
                    "title": "Event to Delete",
                    "description": "Will be deleted",
                    "location": "Test Location",
                    "startDate": "2026-12-31T10:00:00",
                    "endDate": "2026-12-31T18:00:00",
                    "capacity": 50
                }
                """;

        int eventId = given()
                .contentType(ContentType.JSON)
                .body(eventJson)
                .when().post("/api/events")
                .then()
                .statusCode(201)
                .extract().path("id");

        // Delete the event
        given()
                .when().delete("/api/events/" + eventId)
                .then()
                .statusCode(204);

        // Verify it's deleted
        given()
                .when().get("/api/events/" + eventId)
                .then()
                .statusCode(404);
    }
}
