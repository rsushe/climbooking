--liquibase formatted sql

--changeset rsushe:add_insert_climber splitStatements:false
CREATE OR REPLACE FUNCTION add_climber(climber_name VARCHAR, birthday DATE, sport_category VARCHAR, category_id INTEGER)
    RETURNS VOID AS $$
BEGIN
    INSERT INTO climber (name, birthday, sport_category, category_id)
    VALUES (climber_name, birthday, cast(sport_category as sport_category_enum), category_id);
END;
$$ LANGUAGE plpgsql;

ALTER TABLE climber ADD FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE;
