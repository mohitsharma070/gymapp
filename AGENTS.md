# AGENTS.md

Guidelines for AI agents and contributors working in this repository.

## Project

- Name: `gymapp`
- Stack: Java 21, Spring Boot, Maven, PostgreSQL
- Base package: `com.gymapp`

## Architecture

Keep code modular by feature:

- `auth`, `user`, `workout`, `diet`, `progress`, `subscription`, `payment`, `admin`, `storage`
- Shared code in `common`
- App/security configuration in `config`

Each feature should follow:

- `controller` (API layer)
- `service` (business logic)
- `repository` (data access)
- `entity` (JPA models)
- `dto` (request/response payloads)

## Coding Rules

- Use package naming under `com.gymapp.<module>`.
- Prefer DTOs for API responses; avoid returning entities directly.
- Keep controllers thin; place logic in services.
- Throw domain exceptions from `common.exception`.
- Use `ApiResponse<T>` for consistent API response shape.
- Reuse `BaseEntity` for auditable entities.

## Security Notes

- `/api/auth/**` is public.
- Other endpoints require authentication by `SecurityConfig`.
- Current JWT/payment/storage integrations are scaffold level and need production hardening.

## Build & Verify

Compile:

```bash
./mvnw -q -DskipTests compile
```

Run:

```bash
./mvnw spring-boot:run
```

## Contribution Safety

- Do not delete or rename modules unless requested.
- Make focused edits; avoid broad refactors without approval.
- Preserve existing API routes unless explicitly changed.
- Add validation annotations for incoming request DTOs.
- If adding new endpoints, update `README.md`.

## Recommended Next Improvements

- Replace placeholder JWT generation/validation with real signed JWT.
- Replace mock Razorpay/Cloudinary implementations with SDK integrations.
- Add admin DTOs to avoid exposing sensitive entity fields.
- Add unit and integration tests per module.
