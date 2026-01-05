# Event Management API - Quarkus Guide

A comprehensive example of building a REST API with Quarkus framework for managing events. This project demonstrates key
Quarkus features including RESTEasy Reactive, Hibernate ORM with Panache, Liquibase for database migrations, Bean
Validation, OpenAPI/Swagger, and GraalVM native compilation.

## Table of Contents

- [What is Quarkus?](#what-is-quarkus)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Testing the API](#testing-the-api)
- [Key Quarkus Features](#key-quarkus-features)
- [Database Configuration](#database-configuration)
- [Database Migrations with Liquibase](#database-migrations-with-liquibase)
- [Building for Production](#building-for-production)
- [GraalVM Native Image](#graalvm-native-image)
- [Learn More](#learn-more)

## What is Quarkus?

Quarkus is a Kubernetes-native Java framework tailored for GraalVM and OpenJDK HotSpot. It's designed to work with
popular Java standards, frameworks, and libraries like CDI, Hibernate, RESTEasy, and more.

**Key Benefits:**

- **Supersonic Subatomic Java**: Fast startup time and low memory footprint
- **Developer Joy**: Live reload, unified configuration, and great tooling
- **Cloud-Native**: Optimized for containers and Kubernetes
- **Standards-Based**: Leverages familiar Java standards and frameworks
- **GraalVM Native**: Compile to native executables for lightning-fast performance

## Prerequisites

- **Java 21** (GraalVM recommended for native image compilation)
- **Maven 3.8.1+**
- **Docker** (optional, for containerization)
- **GraalVM 21** (optional, for native compilation)

### Installing GraalVM

1. Download GraalVM from [https://www.graalvm.org/downloads/](https://www.graalvm.org/downloads/)
2. Set `JAVA_HOME` to your GraalVM installation
3. For native image support, install native-image:
   ```bash
   gu install native-image
   ```

## Project Structure

```
event-management/
├── src/
│   ├── main/
│   │   ├── java/com/example/events/
│   │   │   ├── entity/
│   │   │   │   └── Event.java              # JPA Entity with Panache
│   │   │   ├── dto/
│   │   │   │   ├── EventDTO.java           # Data Transfer Object for input
│   │   │   │   └── EventResponseDTO.java   # Data Transfer Object for output
│   │   │   ├── service/
│   │   │   │   └── EventService.java       # Business logic layer
│   │   │   └── resource/
│   │   │       └── EventResource.java      # REST endpoints
│   │   └── resources/
│   │       ├── application.properties      # Configuration
│   │       └── import-dev.sql              # Dev data
│   └── test/
├── pom.xml                                 # Maven configuration
└── README.md
```

## Getting Started

### Clone and Build

```bash
# Navigate to project directory
cd how-to-quarkus

# Compile the project
mvn clean compile

# Run tests (optional)
mvn test
```

## Running the Application

### Development Mode (with live reload)

Quarkus comes with a built-in development mode that supports live reload. Changes to code are automatically picked up:

```bash
mvn quarkus:dev
```

**Features in Dev Mode:**

- Automatic recompilation on code changes
- In-memory H2 database with sample data
- Dev UI available at http://localhost:8080/q/dev/
- Swagger UI available at http://localhost:8080/swagger-ui/

### Production Mode (JVM)

```bash
# Package the application
mvn package

# Run the JAR
java -jar target/quarkus-app/quarkus-run.jar
```

### Using Docker

```bash
# Build JVM image
docker build -f src/main/docker/Dockerfile.jvm -t event-management:jvm .

# Run container
docker run -i --rm -p 8080:8080 event-management:jvm
```

## API Endpoints

### Events Management

| Method | Endpoint                                  | Description          |
|--------|-------------------------------------------|----------------------|
| GET    | `/api/events`                             | Get all events       |
| GET    | `/api/events/{id}`                        | Get event by ID      |
| POST   | `/api/events`                             | Create new event     |
| PUT    | `/api/events/{id}`                        | Update event         |
| DELETE | `/api/events/{id}`                        | Delete event         |
| PATCH  | `/api/events/{id}/status?status={STATUS}` | Update event status  |
| GET    | `/api/events/status/{status}`             | Get events by status |

### Event Status Values

- `SCHEDULED` - Event is planned
- `ONGOING` - Event is currently happening
- `COMPLETED` - Event has finished
- `CANCELLED` - Event was cancelled

## Testing the API

### Using cURL

**Create an Event:**

```bash
curl -X POST http://localhost:8080/api/events \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Kubernetes Workshop",
    "description": "Learn Kubernetes basics",
    "location": "Tech Hub",
    "startDate": "2026-05-20T14:00:00",
    "endDate": "2026-05-20T18:00:00",
    "capacity": 30
  }'
```

**Get All Events:**

```bash
curl http://localhost:8080/api/events
```

**Get Event by ID:**

```bash
curl http://localhost:8080/api/events/1
```

**Update Event Status:**

```bash
curl -X PATCH "http://localhost:8080/api/events/1/status?status=ONGOING"
```

**Delete Event:**

```bash
curl -X DELETE http://localhost:8080/api/events/1
```

### Using Swagger UI

Navigate to http://localhost:8080/swagger-ui/ for an interactive API documentation and testing interface.

## Key Quarkus Features

### 1. Hibernate ORM with Panache

Panache simplifies Hibernate ORM by providing an Active Record pattern:

```java

@Entity
public class Event extends PanacheEntity {
    public String title;
    // ... fields are public, no getters/setters needed
}

// Usage in code
Event.

listAll();                    // Find all
Event.

findById(id);                 // Find by ID
Event.

list("status",status);       // Query with parameters
event.

persist();                     // Save or update
event.

delete();                      // Delete
```

### 2. RESTEasy Reactive (Quarkus REST)

Modern reactive REST framework with minimal overhead:

```java

@Path("/api/events")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EventResource {
    // Endpoints automatically handle JSON serialization
}
```

### 3. Dependency Injection (CDI)

Standard Jakarta EE dependency injection:

```java

@ApplicationScoped
public class EventService {
}

@Inject
EventService eventService;
```

### 4. Bean Validation

Automatic validation using Jakarta Bean Validation:

```java
public class EventDTO {
    @NotBlank(message = "Event title is required")
    public String title;

    @Future(message = "Start date must be in the future")
    public LocalDateTime startDate;
}
```

### 5. Liquibase Database Migrations

Version-controlled database schema management:

```xml

<changeSet id="1" author="dev-team">
    <createTable tableName="events">
        <column name="id" type="BIGINT" autoIncrement="true">
            <constraints primaryKey="true"/>
        </column>
        <column name="title" type="VARCHAR(255)">
            <constraints nullable="false"/>
        </column>
    </createTable>
</changeSet>
```

Features:

- Automatic migrations at startup
- Database-independent change tracking
- Rollback support
- Separate contexts for dev/test/prod
- Integration with Hibernate ORM

### 6. OpenAPI and Swagger

Automatic API documentation generation:

```java

@Tag(name = "Event Management")
@Operation(summary = "Get all events")
@APIResponse(responseCode = "200", description = "Success")
public Response getAllEvents() {
}
```

### 7. Unified Configuration

All configuration in `application.properties`:

```properties
# Profile-specific configuration
%dev.quarkus.datasource.db-kind=h2
%prod.quarkus.datasource.db-kind=postgresql
# Environment variable support
%prod.quarkus.datasource.username=${DB_USERNAME:postgres}
```

## Database Configuration

### Development (H2 In-Memory)

By default, the app uses H2 in development mode:

- Database schema managed by Liquibase migrations
- Sample data loaded via Liquibase changesets
- Database is cleaned and recreated on each restart
- No installation required

### Production (PostgreSQL)

For production, configure PostgreSQL using environment variables:

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=eventsdb
export DB_USERNAME=postgres
export DB_PASSWORD=yourpassword
```

Or run with Docker:

```bash
docker run -d \
  -e POSTGRES_DB=eventsdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16
```

## Database Migrations with Liquibase

This project uses Liquibase for database schema versioning and migrations. Liquibase provides a database-independent way
to track, version, and deploy database changes.

### Why Liquibase?

- **Version Control**: Database changes are tracked just like code
- **Reproducible**: Same migrations run identically across all environments
- **Database Independent**: Works with PostgreSQL, H2, MySQL, and more
- **Rollback Support**: Safely rollback changes when needed
- **Team Collaboration**: Prevents conflicts in database schema changes

### Changelog Structure

```
src/main/resources/db/changelog/
├── db.changelog-master.xml          # Master file that includes all migrations
└── changes/
    ├── V1.0.0__initial_schema.xml   # Initial database schema
    └── V1.0.1__sample_data.xml      # Sample data for dev/test
```

### How Migrations Work

1. **Startup**: Liquibase runs automatically when the application starts
2. **Check**: Liquibase checks which changesets have been applied
3. **Execute**: New changesets are executed in order
4. **Track**: Applied changes are recorded in `DATABASECHANGELOG` table

### Configuration

Liquibase is configured in `application.properties`:

```properties
# Enable Liquibase migrations at startup
quarkus.liquibase.migrate-at-start=true
# Location of the master changelog file
quarkus.liquibase.change-log=db/changelog/db.changelog-master.xml
# Validate migrations before applying
quarkus.liquibase.validate-on-migrate=true
# Dev: Clean database and reapply all migrations
%dev.quarkus.liquibase.clean-at-start=true
%dev.quarkus.liquibase.contexts=dev
# Prod: Safe migrations only, no cleaning
%prod.quarkus.liquibase.clean-at-start=false
```

### Creating New Migrations

To add a new database change:

1. **Create a new changelog file** in `src/main/resources/db/changelog/changes/`:
   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                      xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
                      http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd">

       <changeSet id="add-event-category" author="your-name">
           <comment>Add category column to events table</comment>

           <addColumn tableName="events">
               <column name="category" type="VARCHAR(50)">
                   <constraints nullable="true"/>
               </column>
           </addColumn>

           <rollback>
               <dropColumn tableName="events" columnName="category"/>
           </rollback>
       </changeSet>
   </databaseChangeLog>
   ```

2. **Include it in the master changelog** (`db.changelog-master.xml`):
   ```xml
   <include file="db/changelog/changes/V1.2.0__add_categories.xml"/>
   ```

3. **Restart the application** - Liquibase will apply the new migration automatically

### Common Liquibase Operations

**View migration status:**

```bash
mvn liquibase:status
```

**Generate SQL without applying:**

```bash
mvn liquibase:updateSQL
```

**Rollback last changeset:**

```bash
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

**Clear checksums (if needed):**

```bash
mvn liquibase:clearCheckSums
```

### Hibernate ORM Integration

- **Hibernate** is configured with `database.generation=none`
- **Liquibase** manages all schema changes
- **Hibernate Panache** handles data access (queries, CRUD operations)
- This separation provides better control and auditability

### Best Practices

1. **Never modify applied changesets** - Always create new ones
2. **Use meaningful IDs** - Include version number and description
3. **Include rollback instructions** - Makes it safer to revert changes
4. **Test migrations** - Test in dev before applying to production
5. **Use contexts** - Separate dev/test data from production migrations
6. **Version your changesets** - Follow semantic versioning (V1.0.0, V1.1.0, etc.)

For more details, see [LIQUIBASE.md](LIQUIBASE.md).

## Building for Production

### JVM Mode (Traditional)

```bash
# Package application
mvn package

# Creates: target/quarkus-app/quarkus-run.jar
# Run with:
java -jar target/quarkus-app/quarkus-run.jar
```

### Uber-JAR (Single JAR)

```bash
# Build single JAR with all dependencies
mvn package -Dquarkus.package.type=uber-jar

# Creates: target/*-runner.jar
# Run with:
java -jar target/*-runner.jar
```

## GraalVM Native Image

Native compilation produces a standalone executable with:

- **Instant startup** (~0.1s vs 3-10s JVM)
- **Minimal memory** (~50MB vs 200-500MB JVM)
- **No JVM required** at runtime

### Prerequisites

1. Install GraalVM with native-image support
2. Set GRAALVM_HOME environment variable

### Build Native Executable

```bash
# Build native image (takes 3-5 minutes)
mvn package -Pnative

# Creates: target/*-runner
# Run with:
./target/*-runner
```

### Native Image with Docker

```bash
# Build using container (no local GraalVM needed)
mvn package -Pnative -Dquarkus.native.container-build=true

# Build Docker image
docker build -f src/main/docker/Dockerfile.native -t event-management:native .

# Run container
docker run -i --rm -p 8080:8080 event-management:native
```

### Performance Comparison

| Metric       | JVM Mode   | Native Mode |
|--------------|------------|-------------|
| Startup Time | ~3-10s     | ~0.1s       |
| Memory (RSS) | ~200-500MB | ~50-100MB   |
| Image Size   | ~250MB     | ~50MB       |
| Build Time   | ~30s       | ~3-5min     |

## Learn More

### Quarkus Documentation

- [Quarkus Official Guide](https://quarkus.io/guides/)
- [Getting Started](https://quarkus.io/get-started/)
- [All Configuration Options](https://quarkus.io/guides/all-config)

### Key Guides Used in This Project

- [RESTEasy Reactive Guide](https://quarkus.io/guides/rest-json)
- [Hibernate ORM with Panache](https://quarkus.io/guides/hibernate-orm-panache)
- [Validation Guide](https://quarkus.io/guides/validation)
- [OpenAPI and Swagger](https://quarkus.io/guides/openapi-swaggerui)
- [Building Native Image](https://quarkus.io/guides/building-native-image)
- [Container Images](https://quarkus.io/guides/container-image)

### GraalVM Resources

- [GraalVM Official Site](https://www.graalvm.org/)
- [Native Image Documentation](https://www.graalvm.org/latest/reference-manual/native-image/)

## Tips and Best Practices

1. **Use Dev Mode**: Always develop with `mvn quarkus:dev` for instant feedback
2. **Profile Configuration**: Use `%dev`, `%test`, `%prod` prefixes for environment-specific config
3. **Panache Simplicity**: Leverage Panache patterns to reduce boilerplate
4. **Validation**: Use Bean Validation annotations for input validation
5. **OpenAPI**: Document your APIs with OpenAPI annotations
6. **Native Testing**: Test native builds before production deployment
7. **Container Optimization**: Use multi-stage builds for smaller images

## Troubleshooting

**Q: Port 8080 already in use**

```bash
# Change port in application.properties
quarkus.http.port=8081
```

**Q: Database connection errors**

```bash
# Check database is running and credentials are correct
# Verify environment variables are set
```

**Q: Native build fails**

```bash
# Ensure GraalVM and native-image are installed
# Check GRAALVM_HOME is set correctly
# Try container build: -Dquarkus.native.container-build=true
```

---

**Built with Quarkus - Supersonic Subatomic Java** ⚡
