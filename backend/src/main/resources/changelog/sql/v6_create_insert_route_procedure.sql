--liquibase formatted sql

--changeset rsushe:add_create_route_procedure splitStatements:false
CREATE OR REPLACE PROCEDURE create_route(
    place_id INTEGER,
    name VARCHAR,
    difficulty VARCHAR,
    type VARCHAR,
    creation_date date
)
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    INSERT INTO route(place_id, name, difficulty, type, creation_date)
    VALUES (place_id, name, cast(difficulty as difficulty_enum), cast(type as route_type_enum), creation_date);
END;
$$;
