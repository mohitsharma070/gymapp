# GymApp Backend

Spring Boot backend for a gym and fitness platform with modules for auth, users, workouts, diet plans, progress tracking, subscriptions, payments, admin, storage, and shared infrastructure.

## Tech Stack

- Java 21
- Spring Boot 3.x
- Spring Web
- Spring Data JPA
- Spring Security
- PostgreSQL
- Maven

## Project Structure

`src/main/java/com/gymapp`

- `auth` - register/login APIs
- `user` - user profile APIs
- `security` - JWT/authentication scaffolding
- `workout` - workout plans and exercises
- `diet` - diet plans and meals
- `progress` - progress logs and progress photos
- `subscription` - user subscription lifecycle
- `payment` - payment order + verification flow
- `admin` - admin dashboard and data listing
- `storage` - file upload abstraction (Cloudinary scaffold)
- `common` - shared DTOs, base entity, exceptions, enums
- `config` - security, CORS, app properties, OpenAPI config placeholder

## Getting Started

### 1. Prerequisites

- Java 21
- Maven 3.9+
- PostgreSQL

### 2. Run the app

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

### 3. Compile check

```bash
./mvnw -q -DskipTests compile
```

## Configuration

Main app config goes in:

- `src/main/resources/application.properties`

Example values:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gymapp
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

app.jwt.secret=change-this-secret-key
app.jwt.expiration-ms=86400000
```

## API Endpoints

### Auth

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/auth/forgot-password`
- `POST /api/auth/reset-password`
- `POST /api/auth/change-password` (requires JWT)

### User

- `GET /api/users/me` (currently uses `X-USER-ID` header)
- `GET /api/users/{userId}`
- `PUT /api/users/{userId}`

### Workout

- `GET /api/workouts`
- `GET /api/workouts/{id}`
- `POST /api/workouts/{id}/complete`

### Diet

- `GET /api/diet-plans`
- `GET /api/meals`

### Progress

- `POST /api/progress`
- `GET /api/progress`
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
- `GET /api/admin/exercises`
- `GET /api/admin/workout-plans`
- `GET /api/admin/diet-plans`
- `GET /api/admin/payments`

## Notes

- Current JWT and payment integrations are scaffold-level implementations and should be upgraded for production.
- Some endpoints currently use `X-USER-ID` as a temporary user context mechanism.
- Admin endpoints currently return entity data directly; add DTOs before production use.
