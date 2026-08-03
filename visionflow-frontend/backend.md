# VisionFlow — Vision Therapy Management System

A RESTful backend for managing vision therapy workflows — patients, doctors, assessments, therapy plans, sessions, appointments, notifications, and reporting.

Built with **Spring Boot 3.4**, **PostgreSQL**, **JPA + MyBatis**, and **JWT authentication**.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.4.1 |
| Security | Spring Security + JJWT 0.12.6 |
| ORM (writes) | Spring Data JPA / Hibernate |
| ORM (reads) | MyBatis 3 (XML mappers) |
| Database | PostgreSQL |
| Migrations | Flyway (disabled in dev — Hibernate `ddl-auto: create`) |
| Mapping | MapStruct 1.6.3 |
| Boilerplate | Lombok |
| API Docs | SpringDoc OpenAPI 2.7 (Swagger UI) |
| Build | Maven |

---

## Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 14+ running locally

---

## Getting Started

### 1. Create the database

```sql
CREATE DATABASE visionflow;
```

### 2. Configure environment variables (optional)

The app uses sensible defaults for local development. Override via env vars if needed:

| Variable | Default |
|---|---|
| `DB_USERNAME` | `postgres` |
| `DB_PASSWORD` | `postgres` |
| `JWT_SECRET_KEY` | (built-in dev key) |
| `ADMIN_EMAIL` | `admin@visionflow.com` |
| `ADMIN_PASSWORD` | `Admin@1234` |
| `ADMIN_FIRST_NAME` | `Admin` |
| `ADMIN_LAST_NAME` | `User` |

### 3. Run

```bash
./mvnw spring-boot:run
```

The server starts on **`http://localhost:7777/api`**.

On first startup, Hibernate creates the schema and `AdminSeeder` creates the default admin user automatically.

### 4. Swagger UI

```
http://localhost:7777/api/swagger-ui.html
```

---

## Default Admin Credentials

```
Email:    admin@visionflow.com
Password: Admin@1234
```

Use `POST /api/auth/login` to get a JWT, then click **Authorize** in Swagger UI.

---

## Project Structure

```
src/main/java/com/visionflow/
├── annotation/          # @SuccessMessage custom annotation
├── api/                 # Global response wrapper (ApiResponse, ResponseWrapperAdvice)
├── config/
│   ├── security/        # JWT filter, SecurityConfig, UserPrincipal
│   ├── AdminSeeder.java # Seeds default admin on startup
│   ├── JpaAuditingConfig.java
│   └── OpenApiConfig.java
├── constant/            # MessageConstant
├── core/
│   ├── auth/            # User entity, registration, login, token refresh
│   ├── doctor/          # Doctor profiles, specializations
│   ├── patient/         # Patient profiles, doctor assignment
│   ├── assessment/      # Vision assessments (VA, refraction, binocular, IOP)
│   ├── therapyplan/     # Therapy plans + exercises
│   ├── session/         # Therapy sessions + exercise logs
│   ├── appointment/     # Appointment scheduling + lifecycle
│   ├── notification/    # In-app notifications
│   └── report/          # Patient progress, doctor workload, clinic overview
├── exception/           # GlobalExceptionHandler, GenericUncheckedException
└── shared/
    └── entity/          # BaseEntity (id, createdAt, updatedAt, deleted)

src/main/resources/
├── mybatis/mapper/      # MyBatis XML read mappers
├── db/migration/        # Flyway SQL migrations (V1–V8)
└── application.yaml
```

---

## Modules

### Auth (`/auth`, `/users`)
- Register, login, refresh token
- JWT-based stateless authentication
- Role-based access: `ADMIN`, `DOCTOR`, `PATIENT`

### Doctors (`/doctors`)
- Create/update doctor profiles linked to a `DOCTOR`-role user
- Filter by specialization, active status
- Deactivate (soft delete)

### Patients (`/patients`)
- Create/update patient profiles linked to a `PATIENT`-role user
- Assign a primary doctor
- Filter by doctor, gender, active status

### Assessments (`/assessments`)
- Full vision assessments: visual acuity, refraction (sphere/cylinder/axis), binocular vision, IOP
- Status lifecycle: `DRAFT → COMPLETED → REVIEWED`, `CANCELLED` terminal
- Triggers `ASSESSMENT_COMPLETED` / `ASSESSMENT_REVIEWED` notifications

### Therapy Plans (`/therapy-plans`)
- Plans with multiple exercises (type, sets, reps, frequency)
- Status lifecycle: `ACTIVE → COMPLETED`, `CANCELLED` terminal
- One active plan per patient enforced
- Triggers `THERAPY_PLAN_ASSIGNED` / `THERAPY_PLAN_UPDATED` notifications

### Sessions (`/sessions`)
- Therapy sessions linked to a plan, with per-exercise logs
- Status lifecycle: `SCHEDULED → IN_PROGRESS → COMPLETED`, `CANCELLED` terminal
- Auto-increments session number within a plan
- Triggers `SESSION_SCHEDULED` / `SESSION_COMPLETED` notifications

### Appointments (`/appointments`)
- Appointment scheduling with conflict detection (no overlapping doctor slots)
- Status lifecycle: `REQUESTED → CONFIRMED → IN_PROGRESS → COMPLETED`, `CANCELLED` / `NO_SHOW` terminal
- Optional links to an assessment or therapy plan
- Triggers `APPOINTMENT_REQUESTED` / `APPOINTMENT_CONFIRMED` / `APPOINTMENT_CANCELLED` notifications

### Notifications (`/notifications`)
- In-app notifications per user
- Mark as read, mark all as read, delete, unread count
- No path param needed — recipient resolved from JWT principal

### Reports (`/reports`)
- `GET /reports/patients/{id}/progress` — sessions, exercises, completion rate, modal performance
- `GET /reports/doctors/{id}/workload` — appointments by status, sessions, assessments, active plans
- `GET /reports/clinic/overview?from=&to=` — clinic-wide counts with optional date range

---

## API Overview

All responses are wrapped:

```json
{
  "status": 200,
  "message": "assessment fetched successfully!",
  "data": { ... }
}
```

### Authentication

| Method | Endpoint | Access |
|---|---|---|
| POST | `/auth/register` | Public |
| POST | `/auth/login` | Public |
| POST | `/auth/refresh` | Public |
| GET | `/users/{id}` | ADMIN, DOCTOR, self |
| GET | `/users` | ADMIN, DOCTOR |
| PATCH | `/users/{id}/deactivate` | ADMIN |

### Doctors

| Method | Endpoint | Access |
|---|---|---|
| POST | `/doctors` | ADMIN |
| PUT | `/doctors/{id}` | ADMIN, owner DOCTOR |
| GET | `/doctors/{id}` | ADMIN, DOCTOR |
| GET | `/doctors` | ADMIN, DOCTOR |
| PATCH | `/doctors/{id}/deactivate` | ADMIN |

### Patients

| Method | Endpoint | Access |
|---|---|---|
| POST | `/patients` | ADMIN, DOCTOR |
| PUT | `/patients/{id}` | ADMIN, DOCTOR, self |
| PATCH | `/patients/{id}/assign-doctor` | ADMIN, DOCTOR |
| GET | `/patients/{id}` | ADMIN, DOCTOR, self |
| GET | `/patients` | ADMIN, DOCTOR |
| PATCH | `/patients/{id}/deactivate` | ADMIN |

### Assessments

| Method | Endpoint | Access |
|---|---|---|
| POST | `/assessments` | ADMIN, DOCTOR |
| PUT | `/assessments/{id}` | ADMIN, DOCTOR |
| GET | `/assessments/{id}` | All roles |
| GET | `/assessments` | ADMIN, DOCTOR |
| GET | `/assessments/patient/{id}` | ADMIN, DOCTOR, self |
| PATCH | `/assessments/{id}/cancel` | ADMIN, DOCTOR |

### Therapy Plans

| Method | Endpoint | Access |
|---|---|---|
| POST | `/therapy-plans` | ADMIN, DOCTOR |
| PUT | `/therapy-plans/{id}` | ADMIN, DOCTOR |
| GET | `/therapy-plans/{id}` | All roles |
| GET | `/therapy-plans` | ADMIN, DOCTOR |
| GET | `/therapy-plans/patient/{id}` | ADMIN, DOCTOR, self |
| PATCH | `/therapy-plans/{id}/cancel` | ADMIN, DOCTOR |

### Sessions

| Method | Endpoint | Access |
|---|---|---|
| POST | `/sessions` | ADMIN, DOCTOR |
| PUT | `/sessions/{id}` | ADMIN, DOCTOR |
| GET | `/sessions/{id}` | All roles |
| GET | `/sessions` | All roles |
| GET | `/sessions/plan/{planId}` | All roles |
| PATCH | `/sessions/{id}/start` | ADMIN, DOCTOR |
| PATCH | `/sessions/{id}/complete` | ADMIN, DOCTOR |
| PATCH | `/sessions/{id}/cancel` | ADMIN, DOCTOR |

### Appointments

| Method | Endpoint | Access |
|---|---|---|
| POST | `/appointments` | All roles |
| PUT | `/appointments/{id}` | ADMIN, DOCTOR |
| GET | `/appointments/{id}` | All roles |
| GET | `/appointments` | ADMIN, DOCTOR |
| GET | `/appointments/patient/{id}` | ADMIN, DOCTOR, self |
| GET | `/appointments/doctor/{id}` | ADMIN, DOCTOR |
| PATCH | `/appointments/{id}/confirm` | ADMIN, DOCTOR |
| PATCH | `/appointments/{id}/start` | ADMIN, DOCTOR |
| PATCH | `/appointments/{id}/complete` | ADMIN, DOCTOR |
| PATCH | `/appointments/{id}/cancel` | All roles |
| PATCH | `/appointments/{id}/no-show` | ADMIN, DOCTOR |

### Notifications

| Method | Endpoint | Access |
|---|---|---|
| GET | `/notifications` | Authenticated |
| GET | `/notifications/unread` | Authenticated |
| GET | `/notifications/unread/count` | Authenticated |
| PATCH | `/notifications/{id}/read` | Authenticated |
| PATCH | `/notifications/read-all` | Authenticated |
| DELETE | `/notifications/{id}` | Authenticated |

### Reports

| Method | Endpoint | Access |
|---|---|---|
| GET | `/reports/patients/{id}/progress` | ADMIN, DOCTOR, self |
| GET | `/reports/doctors/{id}/workload` | ADMIN, DOCTOR |
| GET | `/reports/clinic/overview` | ADMIN |

---

## Architecture Decisions

**Dual ORM pattern** — JPA for all writes and entity lifecycle management; MyBatis XML mappers for all reads. This keeps write logic clean and type-safe while giving full SQL control for complex projections and aggregations.

**Soft deletes** — `BaseEntity` carries a `deleted` boolean. No data is ever physically removed. All queries filter `deleted = false`.

**Stateless JWT** — Access token (15 min) + refresh token (7 days). No server-side session state.

**Role hierarchy** — `ADMIN` > `DOCTOR` > `PATIENT`. Method-level `@PreAuthorize` with `@doctorSecurity` / `@patientSecurity` beans for ownership checks.

**Notification pattern** — `NotificationService.send()` is called directly in service layer after state transitions. Recipients: doctor gets `APPOINTMENT_REQUESTED`; patient gets all other events.

**AdminSeeder** — `ApplicationRunner` that creates the default admin once on startup. Fully idempotent — skips if email already exists.

---

## Author

**bikalpa.chaudharii** — VisionFlow, 2026
