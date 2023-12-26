--liquibase formatted sql

--changeset rsushe:add_insert_climber splitStatements:false
CREATE OR REPLACE FUNCTION add_climber(climber_name VARCHAR, birthday DATE, sport_category sport_category_enum, category_id INTEGER)
    RETURNS VOID AS $$
BEGIN
    INSERT INTO climber (name, birthday, sport_category, category_id)
    VALUES (climber_name, birthday, sport_category, category_id);
END;
$$ LANGUAGE plpgsql;