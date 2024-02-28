CREATE TABLE IF NOT EXISTS "user"(
    id SERIAL PRIMARY KEY,
    username VARCHAR(100),
    password VARCHAR(100),
    role VARCHAR(100)
)
