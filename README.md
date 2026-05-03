# TrailHub Backend

Spring Boot REST API for trail race management: listing races, registering participants (entries), and admin CRUD for races — backed by PostgreSQL and Flyway.

This repository was built as a **backend coding assignment** (backend-only scope).

## Tech stack

- Java 21+
- Spring Boot 3.3.x
- Spring Web, Spring Data JPA, Spring Security (HTTP Basic)
- PostgreSQL 16 (Docker)
- Flyway
- OpenAPI 3 + Swagger UI (springdoc)

## Prerequisites

- **JDK 21+** (ensure your terminal/IDE and `JAVA_HOME` point to the same JDK you use to run the app)
- **Docker** + Docker Compose
- This repository includes the **Maven Wrapper** (`mvnw` / `mvnw.cmd`) — prefer it if present

## Local database (Docker Compose)

From the repository root:

```bash
docker compose up -d
```
Default connection (see `docker-compose.yml`):

- Host port: `5433` (mapped to container 5432)
- Database: `trailhub`
- User / password: `trailhub` / `trailhub`
- Container name: `trailhub-postgres`
- Stop containers (keeps data volume):

```bash
docker compose down
```
Reset database completely (deletes Docker volume / data):

```bash
docker compose down -v
```

## Configuration
Runtime settings are in `src/main/resources/application.yml`:

- API base URL: http://localhost:8080
- JDBC URL: `jdbc:postgresql://localhost:5433/trailhub`
- Flyway enabled; migrations in `src/main/resources/db/migration`
- JPA `ddl-auto: validate` (schema is managed by Flyway)
- If you change the Postgres host port in `docker-compose.yml`, update `spring.datasource.url` accordingly.

## Run the application

1. Start Postgres:
```bash
docker compose up -d
```

2. Run Spring Boot from the project root (same folder as pom.xml):
Windows (PowerShell)

```powershell
.\mvnw.cmd spring-boot:run
```

macOS / Linux

```bash
./mvnw spring-boot:run
```

If the wrapper complains about `JAVA_HOME`, set it to your JDK installation directory (the folder that contains `bin/java.exe`), then retry.

If you don’t use the wrapper, a globally installed Maven works too:

```bash
mvn spring-boot:run
```

## Testing

**Docker must be running** — integration tests use [Testcontainers](https://java.testcontainers.org/) to start a disposable PostgreSQL 16 instance (image is pulled on first run).

From the project root:

Windows (PowerShell)

```powershell
.\mvnw.cmd test
```

macOS / Linux

```bash
./mvnw test
```

What runs:

- **Integration tests** (`RaceApiIT`, `RaceEntryApiIT`): Spring Boot + `MockMvc` + real Postgres in Docker — API behaviour, HTTP Basic, and roles.
- **Unit tests** (`RaceMapperTest`): plain JUnit, no Spring — request/response DTO mapping.

`AbstractIntegrationTest` holds the shared Testcontainers datasource setup; it is not a test class by itself.

## Authentication
The API uses HTTP Basic Authentication.

Seed passwords use Spring Security’s `{noop}` prefix (plaintext) — intended for local development only.


### Seeded users
| Email | Password | Role |
| --- | --- | --- |
| `admin@trailhub.com` | `admin123` | `ADMIN` |
| `john@trailhub.com` | `user123` | `USER` |
| `michael@trailhub.com` | `user123` | `USER` |

### Seeded races

- Spring Trail 5K (FIVE_K)
- Mountain Challenge 10K (TEN_K)
- Forest Half (HALF_MARATHON)
- City Marathon (MARATHON)


### OpenAPI / Swagger UI
When the app is running:


- **Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- **OpenAPI JSON:** [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Click Authorize in Swagger and use Basic Auth with one of the seeded accounts.
Controllers use `@ParameterObject Pageable`, so Swagger exposes pagination as separate query parameters (`page`, `size`, `sort`) instead of one nested `pageable` object.

```bash
GET /api/races?page=0&size=20

Optional sorting: &sort=id,asc (use a real field name; avoid invalid example values like sort=["string"], which can cause errors)
```
## API overview

### Races

- `GET /api/races` — list races (paginated)
- `GET /api/races/me` — list races the current user has joined (paginated; `page`, `size`, `sort` — default order by registration time, newest first)
- `GET /api/races/{id}` — get race by id
- `POST /api/races` — create race (ADMIN)
- `PUT /api/races/{id}` — update race (ADMIN)
- `DELETE /api/races/{id}` — delete race (ADMIN)

### Entries

- `POST /api/races/{raceId}/entries/me` — join the current user to a race
- `GET /api/races/{raceId}/entries/{entryId}` — get one entry by id (for that race)
- `DELETE /api/races/{raceId}/entries/me` — remove the current user from a race
- `GET /api/races/{raceId}/entries` — list entries for a race (paginated)

### Distance values (JSON): 

`FIVE_K`, `TEN_K`, `HALF_MARATHON`, `MARATHON`


## Example requests
List races (authenticated):

```bash
curl -u "john@trailhub.com:user123" "http://localhost:8080/api/races?page=0&size=20"
```


Create race (admin):
```bash
curl -u "admin@trailhub.com:admin123" \
-H "Content-Type: application/json" \
-d '{"name":"Night Ridge Ultra","distance":"MARATHON"}' \
http://localhost:8080/api/races
```

### Troubleshooting

- `JAVA_HOME` errors when running mvnw: point `JAVA_HOME` at the JDK root (folder containing `bin/java`), reopen the terminal, verify with `java -version`.
- `401 Unauthorized`: missing/invalid Basic credentials, or calling protected endpoints without auth.
- `403 Forbidden`: authenticated, but not allowed (for example a normal user calling admin-only endpoints).
- Port conflicts: ensure `8080` (API) and `5433` (Postgres mapping) are free.


### Production / security notes
Do not ship `{noop}` passwords. Replace with proper password hashing (for example BCrypt) and secure credential storage before any real deployment.

### Assignment note
Repository scope follows the assignment brief as a backend-only implementation (REST API + persistence). A separate frontend was out of scope for this submission.
