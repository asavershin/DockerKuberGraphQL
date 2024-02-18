CREATE OR REPLACE FUNCTION update_last_updated_at()
    RETURNS TRIGGER AS '
    BEGIN
        NEW.note_last_updated_at = CURRENT_TIMESTAMP;
        RETURN NEW;
    END;'
    LANGUAGE plpgsql;


CREATE TRIGGER note_last_updated_trigger
    BEFORE UPDATE ON note
    FOR EACH ROW
EXECUTE FUNCTION update_last_updated_at();