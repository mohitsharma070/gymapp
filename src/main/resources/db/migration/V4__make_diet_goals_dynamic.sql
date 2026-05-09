CREATE TABLE IF NOT EXISTS diet_goals (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    display_name VARCHAR(150) NOT NULL
);

INSERT INTO diet_goals (code, display_name)
SELECT DISTINCT
    UPPER(REPLACE(TRIM(goal), ' ', '_')) AS code,
    CASE
        WHEN goal = UPPER(REPLACE(TRIM(goal), ' ', '_')) THEN INITCAP(REPLACE(TRIM(goal), '_', ' '))
        ELSE TRIM(goal)
    END AS display_name
FROM diet_plans
WHERE goal IS NOT NULL
ON CONFLICT (code) DO NOTHING;

ALTER TABLE diet_plans
    ADD COLUMN IF NOT EXISTS goal_id BIGINT;

UPDATE diet_plans dp
SET goal_id = dg.id
FROM diet_goals dg
WHERE dg.code = UPPER(REPLACE(TRIM(dp.goal), ' ', '_'))
  AND dp.goal_id IS NULL;

ALTER TABLE diet_plans
    ALTER COLUMN goal_id SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_diet_plans_goal_id'
    ) THEN
        ALTER TABLE diet_plans
            ADD CONSTRAINT fk_diet_plans_goal_id
            FOREIGN KEY (goal_id) REFERENCES diet_goals (id);
    END IF;
END $$;

ALTER TABLE diet_plans
    ALTER COLUMN goal DROP NOT NULL;

ALTER TABLE diet_plans
    DROP CONSTRAINT IF EXISTS chk_diet_plans_goal;

CREATE INDEX IF NOT EXISTS idx_diet_plans_goal_id
    ON diet_plans (goal_id);
