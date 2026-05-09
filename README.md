# GymApp Backend

Spring Boot backend for a gym and fitness platform with modules for auth, users, workouts, diet plans, progress tracking, subscriptions, payments, admin, storage, and shared infrastructure.

## Tech Stack

- Java 21
- Spring Boot 3.3.x (stable GA)
- Spring Web
- Spring Data JPA
- Spring Security
- Flyway
- PostgreSQL
- Maven

## Project Structure

`src/main/java/com/gymapp`

- `auth` - register/login/password flows
- `user` - user profile APIs
- `security` - JWT authentication and filter
- `workout` - workout plans and exercises
- `diet` - diet plans and meals
- `progress` - progress logs and photos
- `subscription` - subscription lifecycle
- `payment` - payment order + verification
- `admin` - admin dashboard and management APIs
- `storage` - file upload abstraction
- `common` - shared DTOs, base entity, exceptions, constants
- `config` - security/cors/app configuration

## Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL

## Profiles and Configuration

Configuration is split by profile:

- `application.properties` - shared defaults/common settings
- `application-dev.properties` - local developer settings
- `application-prod.properties` - production-safe settings

Default profile is `dev`.

Key behavior:

- `dev`: `spring.jpa.hibernate.ddl-auto=update`
- `prod`: `spring.jpa.hibernate.ddl-auto=validate`
- Flyway enabled in all profiles

## Running the App

### Local development (default `dev`)

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### Explicit profile run

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Build checks

```bash
./mvnw -q -DskipTests compile
./mvnw -q test
```

## Database Migrations (Flyway)

Flyway migrations live in:

- `src/main/resources/db/migration`

Current baseline:

- `V1__baseline.sql`

Rules:

1. If a migration is already applied in shared/staging/prod, do not edit it; create a new versioned file (`V2__...sql`, `V3__...sql`, etc.).
2. For local/pre-release setups that intentionally keep a single baseline, keep `V1__baseline.sql` idempotent and consistent.
3. Keep DDL changes out of ad-hoc runtime scripts; use migrations only.

## API Endpoints (Current)

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`
- `POST /api/auth/reset-password`
- `POST /api/auth/change-password` (JWT required)

### User

- `GET /api/users/me`
- `GET /api/users/me/diet-plan`
- `GET /api/users/me/meal-progress`
- `GET /api/users/{userId}`
- `PUT /api/users/{userId}`

### Workout

- `GET /api/workouts`
- `GET /api/workouts/{id}`
- `POST /api/workouts/{id}/complete` (USER/ADMIN)

### Diet

- `GET /api/diet-plans`
- `GET /api/diet-plans/{id}`
- `GET /api/diet-plans/today`
- `GET /api/meals`
- `POST /api/meals/{mealId}/complete`

### Progress

- `POST /api/progress` (`workoutPlanId` optional for workout-linked logs)
- `GET /api/progress` (all progress logs for current user)
- `GET /api/progress/workouts` (only workout-linked logs)
- `GET /api/progress/workouts/{workoutId}` (workout-linked logs for a specific workout)
- `POST /api/progress/photos`

### Subscription

- `GET /api/subscriptions/me`
- `POST /api/subscriptions/start`
- `POST /api/subscriptions/cancel`

### Payment

- `POST /api/payments/create-order`
- `POST /api/payments/verify`

### Admin

- `GET /api/admin/dashboard`
- `GET /api/admin/users`
- `PUT /api/admin/users/{id}/role`
- `POST /api/admin/users/{userId}/diet-plan/{dietPlanId}`
- `GET /api/admin/exercises`
- `GET /api/admin/workout-plans`
- `GET /api/admin/diet-plans`
- `POST /api/admin/diet-plans`
- `PUT /api/admin/diet-plans/{id}`
- `DELETE /api/admin/diet-plans/{id}`
- `POST /api/admin/diet-plans/{dietPlanId}/meals`
- `PUT /api/admin/meals/{id}`
- `DELETE /api/admin/meals/{id}`
- `GET /api/admin/payments`

## Notes

- API responses use `ApiResponse<T>` consistently.
- Admin APIs use DTO responses and RBAC hardening.
- JWT/payment/storage integrations are scaffold-level and should be hardened before production launch.
