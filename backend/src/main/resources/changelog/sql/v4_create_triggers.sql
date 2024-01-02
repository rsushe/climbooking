--liquibase formatted sql

--changeset otto15:4 splitStatements:false
-- 1
CREATE OR REPLACE FUNCTION check_sponsor_balance()
    RETURNS TRIGGER AS
$$
DECLARE
    current_balance INT;
BEGIN
    current_balance = (SELECT balance FROM Sponsor WHERE id = NEW.sponsor_id);
    IF current_balance < NEW.donation THEN
        RAISE EXCEPTION 'Баланс спонсора превышен.';
    END IF;
    UPDATE Sponsor SET balance = current_balance - NEW.donation WHERE id = NEW.sponsor_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER before_insert_tournament_sponsor
    BEFORE INSERT
    ON Tournament_sponsor
    FOR EACH ROW
EXECUTE FUNCTION check_sponsor_balance();

-- 2
CREATE OR REPLACE FUNCTION update_booking_and_create_notification() RETURNS TRIGGER AS
$$
DECLARE
    tour_period      period;
    affected_booking RECORD;
BEGIN
    tour_period = (SELECT (start_date, end_date)::period FROM tournament WHERE id = NEW.tournament_id);

    FOR affected_booking IN
        SELECT id, climber_id
        FROM Booking
        WHERE route_id = NEW.route_id
          AND status <> 'TOURNAMENT'
          AND ((start_time >= tour_period.start_date AND start_time < tour_period.end_date) OR
               (end_time >= tour_period.start_date AND end_time < tour_period.end_date))
        LOOP
            UPDATE Booking
            SET status = 'UNAVAILABLE_DUE_TO_TOURNAMENT'
            WHERE id = affected_booking.id;

            INSERT INTO Notification (climber_id, status)
            VALUES (affected_booking.climber_id, 'CANCELLED_BECAUSE_OF_TOURNAMENT');
        END LOOP;

    INSERT INTO booking(climber_id, route_id, status, start_time, end_time)
    VALUES ((SELECT climber_id
             FROM tournament_organisator t_o
             WHERE t_o.tournament_id = NEW.tournament_id
             LIMIT 1),
            NEW.route_id,
            'ACTIVE',
            tour_period.start_date,
            tour_period.end_date);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER tr_tournament_route_after_insert
    AFTER INSERT
    ON Tournament_routes
    FOR EACH ROW
EXECUTE PROCEDURE update_booking_and_create_notification();

-- 3
CREATE OR REPLACE FUNCTION delete_booking_notifications() RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status AND NEW.status = 'CANCELLED' THEN
        DELETE FROM Booking_notification WHERE booking_id = OLD.id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER tr_booking_status_after_update
    AFTER UPDATE OF status
    ON Booking
    FOR EACH ROW
EXECUTE PROCEDURE delete_booking_notifications();


-- 4
CREATE OR REPLACE FUNCTION update_notification()
    RETURNS TRIGGER AS
$$
BEGIN
    IF NOT EXISTS(SELECT booking_id FROM booking_notification WHERE notification_id = OLD.notification_id) THEN
        UPDATE notification SET status='BOOKING_IS_AVAILABLE' WHERE id = OLD.notification_id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_delete_booking_notification
    AFTER DELETE
    ON booking_notification
    FOR EACH ROW
EXECUTE FUNCTION update_notification();

-- 5
CREATE OR REPLACE FUNCTION try_insert_achievement()
    RETURNS TRIGGER AS
$$
BEGIN
    IF (SELECT COUNT(*) FROM route_author WHERE climber_id = NEW.climber_id) = 5 THEN
        INSERT INTO climber_achievement VALUES (NEW.climber_id, 'Большой репертуар');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_insert_route_author
    AFTER INSERT
    ON route_author
    FOR EACH ROW
EXECUTE FUNCTION try_insert_achievement();

-- 6
CREATE OR REPLACE FUNCTION update_booking_if_route_rolled()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.is_rolled IS DISTINCT FROM NEW.is_rolled AND NEW.is_rolled = TRUE THEN
        UPDATE booking SET status = 'UNAVAILABLE_ROUTE_IS_ROLLED' WHERE status = 'ACTIVE' and route_id=NEW.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER after_update_route
    AFTER UPDATE
    ON route
    FOR EACH ROW
EXECUTE FUNCTION update_booking_if_route_rolled();

-- 7
CREATE OR REPLACE FUNCTION create_notification_route_is_rolled()
    RETURNS TRIGGER AS
$$
BEGIN
    IF OLD.status IS DISTINCT FROM NEW.status AND OLD.status = 'ACTIVE' AND NEW.status = 'UNAVAILABLE_ROUTE_IS_ROLLED' THEN
        INSERT INTO Notification (climber_id, status)
        VALUES (NEW.climber_id, 'UNAVAILABLE_BECAUSE_ROUTE_IS_ROLLED');
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE TRIGGER route_is_rolled_booking_unavailable
    AFTER UPDATE
    ON booking
    FOR EACH ROW
EXECUTE FUNCTION create_notification_route_is_rolled();
