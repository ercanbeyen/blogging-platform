CREATE TABLE IF NOT EXISTS TICKETS (
    ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    DESCRIPTION VARCHAR(255) NOT NULL,
    CREATED_AT TIMESTAMP NOT NULL,
    UPDATED_AT TIMESTAMP
);