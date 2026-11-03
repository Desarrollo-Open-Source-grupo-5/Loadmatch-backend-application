# LoadMatch Platform

LoadMatch Platform is a RESTful API built on the foundations of Domain-Driven Design (DDD) for **LoadMatch**, the logistics marketplace by **CargoLink Labs** that connects companies in Lima that need to move freight with independent carriers. Developed with Java 27, Spring Boot 4.1.2, and PostgreSQL, the platform is organized into bounded contexts and follows Command Query Responsibility Segregation (CQRS) principles, ensuring a clean separation between data modification and retrieval operations.

---

## Tech Stack

- **Language:** Java 27 (with Project Loom Virtual Threads)
- **Framework:** Spring Boot 4.1.2
- **Database:** PostgreSQL 16
- **Persistence:** Spring Data JPA / Hibernate with Snake Case and Pluralized Table Naming Strategy
- **Concurrency:** Project Loom Virtual Threads (`spring.threads.virtual.enabled=true`)
- **API Standards:** RFC 9457 Problem Details for HTTP APIs
- **Documentation:** SpringDoc OpenAPI (Swagger UI)
- **Build Tool:** Maven 3.10+
- **Containerization:** Docker
- **Cloud:** Microsoft Azure (App Service and Azure Database for PostgreSQL)

---

## Prerequisites

- Java 27 JDK
- Maven 3.10+ (or the included Maven Wrapper)
- PostgreSQL 16+
- Docker (optional)

---

## Project Structure

The project is organized into bounded contexts following Layered Domain-Driven Design (DDD):

```text
src/main/java/com/cargolinklabs/loadmatch/platform/
├── iam/               # Identity and Access Management (Users, Roles, Sign-up, Sign-in)
├── profiles/          # Company and carrier profiles
├── fleet/             # Carrier vehicles
├── freightpublishing/ # Freight requests published by companies
├── matching/          # Matching between freight requests and carriers
├── tripexecution/     # Trip lifecycle and tracking
└── shared/            # Shared kernel (Domain base aggregate, Result type, ProblemDetail, Concurrency, i18n)
```

Each bounded context follows a classic 4-layer structure:
- `domain`: Aggregates, entities, value objects, domain events, and repository port interfaces.
- `application`: Command services, query services, internal implementations, event handlers, and outbound ACL ports.
- `infrastructure`: JPA persistence entities, repository adapters, assemblers, security configurations, and technology adapters.
- `interfaces`: REST controllers, resources, request/response assemblers, and ACL facades.

---

## Configuration

The project uses profile-based configuration located in `src/main/resources`:
- `application.properties`: Shared defaults, database naming strategy, i18n, and Virtual Threads configuration.
- `application-dev.properties`: Development settings (default).
- `application-prod.properties`: Production settings for Azure.

### Environment Variables

Required environment variables (especially for the `prod` profile):

| Variable                 | Description              | Default (Dev) |
|--------------------------|--------------------------|---------------|
| `DATABASE_URL`           | PostgreSQL host address  | `localhost`   |
| `DATABASE_PORT`          | PostgreSQL port          | `5432`        |
| `DATABASE_NAME`          | Database name            | `loadmatch`   |
| `DATABASE_USER`          | Database username        | `postgres`    |
| `DATABASE_PASSWORD`      | Database password        | `password`    |
| `PORT`                   | Application port         | `8080`        |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile    | `dev`         |

---

## Getting Started

### 1) Database Setup
Ensure PostgreSQL is running and create the database specified in `DATABASE_NAME`:

```sql
CREATE DATABASE loadmatch;
```

### 2) Run the Application

#### Using Maven
```bash
# Run with default 'dev' profile
./mvnw clean spring-boot:run

# Run with specific profile and environment variables
SPRING_PROFILES_ACTIVE=prod \
DATABASE_URL=localhost \
DATABASE_PORT=5432 \
DATABASE_NAME=loadmatch \
DATABASE_USER=postgres \
DATABASE_PASSWORD=password \
PORT=8080 \
./mvnw clean spring-boot:run
```

On Windows PowerShell, use `.\mvnw clean spring-boot:run`.

#### Using Docker
```bash
# Build the image
docker build -t loadmatch-platform .

# Run the container
docker run -p 8080:8080 \
  -e DATABASE_URL=host.docker.internal \
  -e DATABASE_NAME=loadmatch \
  -e DATABASE_USER=postgres \
  -e DATABASE_PASSWORD=password \
  loadmatch-platform
```

---

## Scripts and Commands

- `mvn clean compile`: Compile the project.
- `mvn test`: Run unit and repository tests.
- `mvn package`: Package the application into a JAR file.
- `mvn spring-boot:run`: Launch the application.

Note: Fully configured for Java 27 with Project Loom virtual threads enabled.

---

## Architecture & Development Conventions

- **Domain-Driven Design (DDD) & CQRS:** Write operations use `CommandService`, read operations use `QueryService`.
- **Aggregate Encapsulation:** Aggregates protect their invariants without public `@Setter`s, using domain business methods and `reconstitute(...)` factories.
- **Identity Referencing:** Cross-aggregate references use strongly-typed ID value objects.
- **Standardized Domain Event Dispatching:** Aggregates register events via `registerDomainEvent(...)`, which are published by `DomainEventPublisher` upon persistence.
- **Virtual Threads (Project Loom):** Embedded Tomcat and asynchronous executors (`@Async`) run on lightweight virtual threads.
- **RFC 9457 Error Handling:** REST errors return standard `ProblemDetail` responses under `/api/v1/errors/{error-slug}` with localized messages (English and Spanish) and validation field mappings.

---

## Documentation

Refer to the `docs/` folder and project root for detailed specifications:
- [Architecture Decision Records (ADRs)](docs/adrs.md)
- [Contributing Guide](CONTRIBUTING.md)
- [Changelog](CHANGELOG.md)
- Project report: https://github.com/Desarrollo-Open-Source-grupo-5/Loadmatch-report

---

## License

This project is licensed under the MIT License. See the [LICENSE.md](LICENSE.md) file for details.
