--liquibase formatted sql

--changeset rsushe:add_create_tournament_procedure splitStatements:false
CREATE OR REPLACE PROCEDURE create_tournament(
    name VARCHAR,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    status VARCHAR,
    climber_ids INTEGER[],
    route_ids INTEGER[]
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    tournament_id INTEGER;
    climber_id    INTEGER;
    route_id      INTEGER;
BEGIN
    INSERT INTO tournament(name, start_date, end_date, status)
    VALUES (name, start_date, end_date, status)
    RETURNING id into tournament_id;

    FOREACH climber_id IN ARRAY climber_ids
        LOOP
            INSERT INTO tournament_organisator(tournament_id, climber_id)
            VALUES (tournament_id, climber_id);
        END LOOP;

    FOREACH route_id IN ARRAY route_ids
        LOOP
            INSERT INTO tournament_routes(route_id, tournament_id)
            VALUES (route_id, tournament_id);
        END LOOP;
END;
$$;
