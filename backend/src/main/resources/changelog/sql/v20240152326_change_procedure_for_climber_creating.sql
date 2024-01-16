--liquibase formatted sql

--changeset otto15:change_create_climber_procedure splitStatements:false
CREATE OR REPLACE PROCEDURE add_climber(
    climber_name VARCHAR,
    birthday DATE,
    category_id INTEGER
)
    LANGUAGE plpgsql
AS
$$
DECLARE
BEGIN
    INSERT INTO climber (name, birthday, category_id)
    VALUES (climber_name, birthday, category_id);
END;
$$;
