--liquibase formatted sql

--changeset otto15:3 splitStatements:false
CREATE OR REPLACE FUNCTION add_sponsor(name VARCHAR, phone VARCHAR, email VARCHAR, balance INTEGER)
    RETURNS VOID AS $$
BEGIN
    INSERT INTO Sponsor (name, phone, email, balance) VALUES (name, phone, email, balance);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_participant(climber_id INTEGER, tournament_id INTEGER)
    RETURNS VOID AS $$
BEGIN
    INSERT INTO Participant (climber_id, tournament_id) VALUES (climber_id, tournament_id);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_booking(climber_id INTEGER, route_id INTEGER, status VARCHAR, start_time TIMESTAMP, end_time TIMESTAMP)
    RETURNS VOID AS $$
BEGIN
    INSERT INTO Booking (climber_id, route_id, status, start_time, end_time) VALUES (climber_id, route_id, status, start_time, end_time);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add_tournament(name VARCHAR, start_date TIMESTAMP, end_date TIMESTAMP, status VARCHAR)
    RETURNS VOID AS $$
BEGIN
    INSERT INTO Tournament (name, start_date, end_date, status) VALUES (name, start_date, end_date, status);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_tournament_participants(tournament_id_param INTEGER)
    RETURNS TABLE(
                     climber_name VARCHAR,
                     climber_birthday DATE,
                     climber_sport_category VARCHAR
                 ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            c.name,
            c.birthday,
            c.sport_category
        FROM
            Participant p
                JOIN Climber c ON p.climber_id = c.id
        WHERE
                p.tournament_id = tournament_id_param;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_routes_with_authors()
    RETURNS TABLE(
                     route_id INTEGER,
                     route_name VARCHAR,
                     route_difficulty VARCHAR,
                     author_name VARCHAR
                 ) AS $$
BEGIN
    RETURN QUERY
        SELECT
            r.id,
            r.name,
            r.difficulty,
            a.name
        FROM
            Route r
                JOIN Route_author ra ON r.id = ra.route_id
                JOIN Climber a ON ra.climber_id = a.id;
END;
$$ LANGUAGE plpgsql;
