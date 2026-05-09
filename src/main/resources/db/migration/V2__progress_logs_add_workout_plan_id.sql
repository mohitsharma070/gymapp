ALTER TABLE progress_logs
    ADD COLUMN IF NOT EXISTS workout_plan_id BIGINT;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_progress_logs_workout_plan'
    ) THEN
        ALTER TABLE progress_logs
            ADD CONSTRAINT fk_progress_logs_workout_plan
            FOREIGN KEY (workout_plan_id) REFERENCES workout_plans (id);
    END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_progress_logs_workout_plan_id
    ON progress_logs (workout_plan_id);
