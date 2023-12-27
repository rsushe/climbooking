--liquibase formatted sql

--changeset rsushe:add_create_climber_procedure splitStatements:false
CREATE OR REPLACE PROCEDURE add_climber(
    climber_name VARCHAR,
    birthday DATE,
    sport_category VARCHAR,
    category_id INTEGER
)
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    INSERT INTO climber (name, birthday, sport_category, category_id)
    VALUES (climber_name, birthday, cast(sport_category as sport_category_enum), category_id)
    RETURNING id;
END;
$$;

ALTER TABLE climber
    ADD FOREIGN KEY (category_id) REFERENCES category (id) ON DELETE CASCADE;
