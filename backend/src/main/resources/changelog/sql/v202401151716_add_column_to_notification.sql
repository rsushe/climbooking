--liquibase formatted sql

--changeset otto15:change_create_notification_procedure splitStatements:false

CREATE OR REPLACE PROCEDURE create_notification(
    climber_id integer,
    booking_ids integer[]
)
    LANGUAGE plpgsql
AS
$$
DECLARE
    booking_id integer;
    notification_id integer;
BEGIN
    INSERT INTO notification(climber_id, status, route_id) VALUES (climber_id, 'NEW', (SELECT route_id FROM booking WHERE booking.id = ANY(booking_ids))) RETURNING id INTO notification_id;
    FOREACH booking_id IN ARRAY booking_ids
        LOOP
            INSERT INTO booking_notification(notification_id, booking_id)
            VALUES (notification_id, booking_id);
        END LOOP;
END;
$$;
