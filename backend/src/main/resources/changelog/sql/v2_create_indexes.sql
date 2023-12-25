--liquibase formatted sql

--changeset otto15:2
CREATE INDEX IF NOT EXISTS idx_booking_climber_status ON Booking(climber_id, status);

CREATE INDEX IF NOT EXISTS idx_booking ON booking_notification(booking_id);

CREATE INDEX IF NOT EXISTS idx_tournament on tournament(status);
