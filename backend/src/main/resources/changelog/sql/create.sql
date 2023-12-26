--liquibase formatted sql

--changeset rsushe:import_tables_creation

-- Creating the ENUM type for difficulty (assuming possible values; adjust as needed)
CREATE TYPE difficulty_enum AS ENUM ('6a', '6b', '6c', '7a');

-- Creating the ENUM type for route_type (assuming possible values; adjust as needed)
CREATE TYPE route_type_enum AS ENUM ('bouldering', 'lead', 'trad');

-- Creating the ENUM type for sport_category (assuming possible values; adjust as needed)
CREATE TYPE sport_category_enum AS ENUM ('beginner', 'amateur', 'professional');

-- Creating the Climber table
CREATE TABLE IF NOT EXISTS Climber
(
    id             SERIAL PRIMARY KEY,
    name           VARCHAR(50),
    birthday       DATE,
    sport_category sport_category_enum,
    category_id    INT -- This probably references Category(id), placeholder for now
    -- Add other attributes and constraints if necessary
);

-- Creating the Place table
CREATE TABLE IF NOT EXISTS Place
(
    id       SERIAL PRIMARY KEY,
    name     VARCHAR(50),
    type     VARCHAR(50),
    location POINT
    -- Add other attributes and constraints if necessary
);

-- Creating the Category table
CREATE TABLE IF NOT EXISTS Category
(
    id       SERIAL PRIMARY KEY,
    from_age INT,
    to_age   INT,
    name     VARCHAR(50)
);

-- Creating the Sponsor table
CREATE TABLE IF NOT EXISTS Sponsor
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50),
    phone   VARCHAR(50),
    email   VARCHAR(50),
    balance INT
);

-- Creating the Tournament table
CREATE TABLE IF NOT EXISTS Tournament
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50),
    start_date TIMESTAMP,
    end_date   TIMESTAMP,
    status     VARCHAR(50)
);

-- Further tables will be created after establishing these bases.

-- Creating the Route table with foreign key to Place
CREATE TABLE IF NOT EXISTS Route
(
    id            SERIAL PRIMARY KEY,
    place_id      INT,
    name          VARCHAR(50),
    difficulty    difficulty_enum,
    type          route_type_enum,
    creation_date DATE,
    is_rolled     BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (place_id) REFERENCES Place (id) ON DELETE CASCADE
);

-- Creating the Tournament_category table with foreign keys to Tournament and Category
CREATE TABLE IF NOT EXISTS Tournament_category
(
    id            SERIAL PRIMARY KEY,
    tournament_id INT,
    category_id   INT,
    FOREIGN KEY (tournament_id) REFERENCES Tournament (id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES Category (id) ON DELETE CASCADE
);

-- Creating the Tournament_routes table with foreign keys to Tournament and Route
CREATE TABLE IF NOT EXISTS Tournament_routes
(
    route_id      INT,
    tournament_id INT,
    FOREIGN KEY (route_id) REFERENCES Route (id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES Tournament (id) ON DELETE CASCADE,
    PRIMARY KEY (route_id, tournament_id)
);

-- Creating the Tournament_organisator table with foreign keys to Tournament and Organiser
CREATE TABLE IF NOT EXISTS Tournament_organisator
(
    tournament_id INT,
    climber_id    INT,
    FOREIGN KEY (tournament_id) REFERENCES Tournament (id) ON DELETE CASCADE,
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE,
    PRIMARY KEY (tournament_id, climber_id)
);

-- Creating the Tournament_sponsor table with foreign keys to Tournament and Sponsor
CREATE TABLE IF NOT EXISTS Tournament_sponsor
(
    tournament_id INT,
    sponsor_id    INT,
    donation      INT,
    FOREIGN KEY (tournament_id) REFERENCES Tournament (id) ON DELETE CASCADE,
    FOREIGN KEY (sponsor_id) REFERENCES Sponsor (id) ON DELETE CASCADE,
    PRIMARY KEY (tournament_id, sponsor_id)
);

-- The following tables need other tables created in the Part 4 scripts to define their foreign keys.
-- Hold off on creating 'Lead_meta', 'Route_author', 'Route_ascent', 'Booking', 'Booking_notification', 'Participant'

-- Creating the Route_author table with foreign keys to Route and Climber
CREATE TABLE IF NOT EXISTS Route_author
(
    route_id   INT,
    climber_id INT,
    FOREIGN KEY (route_id) REFERENCES Route (id) ON DELETE CASCADE,
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE,
    PRIMARY KEY (route_id, climber_id)
);

-- Creating the Lead_meta table with a foreign key to Route
CREATE TABLE IF NOT EXISTS Lead_meta
(
    id              SERIAL PRIMARY KEY,
    route_id        INT,
    quickdraw_count INT,
    route_height    INT,
    FOREIGN KEY (route_id) REFERENCES Route (id) ON DELETE CASCADE
);

-- Creating the Route_ascent table with foreign keys to Route and Climber
CREATE TABLE IF NOT EXISTS Route_ascent
(
    route_id    INT,
    climber_id  INT,
    ascent_date DATE,
    FOREIGN KEY (route_id) REFERENCES Route (id) ON DELETE CASCADE,
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE,
    PRIMARY KEY (route_id, climber_id)
);

-- Creating the Booking table with foreign keys to Climber and Route
CREATE TABLE IF NOT EXISTS Booking
(
    id         SERIAL PRIMARY KEY,
    climber_id INT,
    route_id   INT,
    status     VARCHAR(50),
    start_time TIMESTAMP,
    end_time   TIMESTAMP,
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES Route (id) ON DELETE CASCADE
);

-- Creating the Notification table
-- Since no table references Notification as a foreign key, you can create this first
CREATE TABLE IF NOT EXISTS Notification
(
    id         SERIAL PRIMARY KEY,
    climber_id INT,
    status     VARCHAR(50),
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE
);

-- Creating the Booking_notification table with foreign keys to Booking and Climber
CREATE TABLE IF NOT EXISTS Booking_notification
(
    notification_id INT,
    booking_id      INT,
    FOREIGN KEY (notification_id) REFERENCES Notification (id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES Booking (id) ON DELETE SET NULL,
    PRIMARY KEY (notification_id, booking_id)
);

-- Creating the Participant table with foreign keys to Climber and Tournament
CREATE TABLE IF NOT EXISTS Participant
(
    climber_id    INT,
    tournament_id INT,
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE,
    FOREIGN KEY (tournament_id) REFERENCES Tournament (id) ON DELETE CASCADE,
    PRIMARY KEY (climber_id, tournament_id)
);

CREATE TABLE IF NOT EXISTS Achievement
(
    title       VARCHAR(50) PRIMARY KEY,
    description TEXT
);

CREATE TABLE IF NOT EXISTS Climber_achievement
(
    climber_id        INT,
    achievement_title VARCHAR(50),
    FOREIGN KEY (climber_id) REFERENCES Climber (id) ON DELETE CASCADE,
    FOREIGN KEY (achievement_title) REFERENCES Achievement (title) ON DELETE CASCADE
);

-- Adding constraints to the Climber table
ALTER TABLE Climber
    ADD CONSTRAINT chk_climber_name CHECK (length(name) > 0), -- Ensure the name is not empty
    ADD CONSTRAINT chk_climber_birthday CHECK (birthday <= CURRENT_DATE);
-- Birthdate should not be in the future

-- Adding UNIQUE constraints to the Place table
ALTER TABLE Place
    ADD CONSTRAINT uq_place_name UNIQUE (name);
-- Place names should be unique

-- Adding constraints to the Route table
ALTER TABLE Route
    ADD CONSTRAINT chk_route_name CHECK (length(name) > 0), -- Ensure the name is not empty
    ADD CONSTRAINT chk_route_difficulty CHECK (difficulty IS NOT NULL), -- Ensure difficulty is set
    ADD CONSTRAINT chk_route_type CHECK (type IS NOT NULL);
-- Ensure type is set

-- Adding constraints to the Booking table
ALTER TABLE Booking
    ADD CONSTRAINT chk_booking_times CHECK (start_time < end_time);
-- Start time should precede end time

-- Adding constraints to the Tournament table
ALTER TABLE Tournament
    ADD CONSTRAINT chk_tournament_dates CHECK (start_date < end_date), -- Start date should precede end date
    ADD CONSTRAINT chk_tournament_status CHECK (status IN ('planned', 'ongoing', 'completed', 'cancelled'));
-- Allowed status values

-- Adding constraints to the Tournament_sponsor table
ALTER TABLE Tournament_sponsor
    ADD CONSTRAINT chk_donation_non_negative CHECK (donation >= 0); -- Donation amounts should be non-negative

CREATE TYPE period AS
(
    start_date timestamp,
    end_date   timestamp
);
