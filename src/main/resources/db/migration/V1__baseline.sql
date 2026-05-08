CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

ALTER TABLE users ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;
CREATE INDEX IF NOT EXISTS idx_users_role_is_active
    ON users (role, is_active);

CREATE TABLE IF NOT EXISTS workout_plans (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    goal VARCHAR(255) NOT NULL,
    scheduled_date DATE,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS exercises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    sets INTEGER NOT NULL,
    reps INTEGER NOT NULL,
    workout_plan_id BIGINT NOT NULL,
    CONSTRAINT fk_exercises_workout_plan
        FOREIGN KEY (workout_plan_id) REFERENCES workout_plans (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_exercises_workout_plan_id
    ON exercises (workout_plan_id);

CREATE TABLE IF NOT EXISTS diet_plans (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    goal VARCHAR(255) NOT NULL,
    plan_date DATE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS meals (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    meal_type VARCHAR(255) NOT NULL,
    calories INTEGER NOT NULL,
    diet_plan_id BIGINT NOT NULL,
    CONSTRAINT fk_meals_diet_plan
        FOREIGN KEY (diet_plan_id) REFERENCES diet_plans (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_meals_diet_plan_id
    ON meals (diet_plan_id);

CREATE TABLE IF NOT EXISTS progress_logs (
    id BIGSERIAL PRIMARY KEY,
    log_date DATE NOT NULL,
    weight DOUBLE PRECISION,
    body_fat_percentage DOUBLE PRECISION,
    notes VARCHAR(1000),
    photo_url VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS subscriptions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    plan_name VARCHAR(255) NOT NULL,
    status VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_subscriptions_user
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX IF NOT EXISTS idx_subscriptions_user_id
    ON subscriptions (user_id);
CREATE INDEX IF NOT EXISTS idx_subscriptions_user_created_at_desc
    ON subscriptions (user_id, created_at DESC);
CREATE INDEX IF NOT EXISTS idx_subscriptions_status
    ON subscriptions (status);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    subscription_id BIGINT NOT NULL,
    order_id VARCHAR(255) NOT NULL,
    payment_id VARCHAR(255),
    amount INTEGER NOT NULL,
    currency VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_payments_subscription
        FOREIGN KEY (subscription_id) REFERENCES subscriptions (id)
);

CREATE INDEX IF NOT EXISTS idx_payments_subscription_id
    ON payments (subscription_id);
CREATE INDEX IF NOT EXISTS idx_payments_order_id
    ON payments (order_id);
CREATE UNIQUE INDEX IF NOT EXISTS idx_payments_order_id_unique
    ON payments (order_id);

CREATE INDEX IF NOT EXISTS idx_progress_logs_log_date
    ON progress_logs (log_date);

ALTER TABLE progress_logs ADD COLUMN IF NOT EXISTS user_id BIGINT REFERENCES users (id);
CREATE INDEX IF NOT EXISTS idx_progress_logs_user_id
    ON progress_logs (user_id);
CREATE INDEX IF NOT EXISTS idx_workout_plans_scheduled_date
    ON workout_plans (scheduled_date);

CREATE TABLE IF NOT EXISTS password_reset_tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP NOT NULL,
    used_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_password_reset_tokens_user
        FOREIGN KEY (user_id) REFERENCES users (id)
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_password_reset_tokens_user_id
    ON password_reset_tokens (user_id);
CREATE INDEX IF NOT EXISTS idx_password_reset_tokens_expires_at
    ON password_reset_tokens (expires_at);
CREATE INDEX IF NOT EXISTS idx_password_reset_tokens_active_by_user
    ON password_reset_tokens (user_id, used_at, expires_at);
