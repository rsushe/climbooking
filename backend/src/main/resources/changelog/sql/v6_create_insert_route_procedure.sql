--liquibase formatted sql

--changeset rsushe:add_create_route_procedures splitStatements:false
CREATE OR REPLACE PROCEDURE create_route(
    place_id INTEGER,
    climber_ids INTEGER[],
    name VARCHAR,
    difficulty VARCHAR,
    type VARCHAR,
    creation_date date
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    climber_id integer;
    route_id INTEGER;
BEGIN
    INSERT INTO route(place_id, name, difficulty, type, creation_date)
    VALUES (place_id, name, cast(difficulty as difficulty_enum), cast(type as route_type_enum), creation_date)
    RETURNING id INTO route_id;

    FOREACH climber_id IN ARRAY climber_ids
        LOOP
            INSERT INTO route_author(route_id, climber_id)
            VALUES (route_id, climber_id);
        END LOOP;
END;
$$;
