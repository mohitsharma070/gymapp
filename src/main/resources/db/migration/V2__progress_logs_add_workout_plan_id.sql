ALTER TABLE progress_logs
    ADD COLUMN IF NOT EXISTS workout_plan_id BIGINT;

ALTER TABLE progress_logs
    ADD CONSTRAINT fk_progress_logs_workout_plan
    FOREIGN KEY (workout_plan_id) REFERENCES workout_plans (id);

CREATE INDEX IF NOT EXISTS idx_progress_logs_workout_plan_id
    ON progress_logs (workout_plan_id);
