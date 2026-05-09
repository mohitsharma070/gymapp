-- Add deleted_at for entities extending BaseEntity.
-- This migration is required for already-initialized databases where V1 already ran.

ALTER TABLE users ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE workout_plans ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE diet_plans ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE meal_progress ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE progress_logs ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE subscriptions ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE payments ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
ALTER TABLE password_reset_tokens ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP;
