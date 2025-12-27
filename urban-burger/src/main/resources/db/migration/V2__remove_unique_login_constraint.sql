DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN (
        SELECT tc.constraint_name
        FROM information_schema.table_constraints tc
        JOIN information_schema.constraint_column_usage ccu ON tc.constraint_name = ccu.constraint_name
        WHERE tc.table_name = 'users' AND ccu.column_name = 'login' AND tc.constraint_type = 'UNIQUE'
    ) LOOP
        EXECUTE 'ALTER TABLE users DROP CONSTRAINT ' || r.constraint_name;
    END LOOP;
END $$;
