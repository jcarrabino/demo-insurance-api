CREATE DATABASE IF NOT EXISTS insurance;
USE insurance;

CREATE TABLE IF NOT EXISTS line (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255),
    description VARCHAR(255),
    max_coverage FLOAT,
    min_coverage FLOAT
);

CREATE TABLE IF NOT EXISTS account (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(255),
    middle_name   VARCHAR(255),
    last_name     VARCHAR(255),
    street        VARCHAR(255),
    city          VARCHAR(255),
    state         VARCHAR(255),
    zip_code      VARCHAR(255),
    country       VARCHAR(255),
    phone_number  INT,
    date_of_birth DATE,
    email         VARCHAR(255) UNIQUE,
    password      VARCHAR(255),
    admin         BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS policy (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    line_id    INT,
    account_id INT,
    premium    DECIMAL(19,2),
    start_date DATE,
    end_date   DATE,
    CONSTRAINT fk_policy_line    FOREIGN KEY (line_id)    REFERENCES line(id),
    CONSTRAINT fk_policy_account FOREIGN KEY (account_id) REFERENCES account(id)
);

CREATE TABLE IF NOT EXISTS account_policy (
    account_id INT NOT NULL,
    policy_id  INT NOT NULL,
    PRIMARY KEY (account_id, policy_id),
    CONSTRAINT fk_ap_account FOREIGN KEY (account_id) REFERENCES account(id),
    CONSTRAINT fk_ap_policy  FOREIGN KEY (policy_id)  REFERENCES policy(id)
);

CREATE TABLE IF NOT EXISTS claim (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    claim_number VARCHAR(255),
    description  VARCHAR(255),
    claim_date   DATE,
    claim_status VARCHAR(50),
    policy_id    INT,
    CONSTRAINT fk_claim_policy FOREIGN KEY (policy_id) REFERENCES policy(id)
);

-- Seed data for lines
INSERT INTO line (id, name, description, max_coverage, min_coverage) VALUES
    (1, 'Auto',     'Automobile insurance coverage',  500000, 10000),
    (2, 'Home',     'Homeowners insurance coverage',  1000000, 50000),
    (3, 'Life',     'Life insurance coverage',        2000000, 100000),
    (4, 'Health',   'Health insurance coverage',      500000, 5000)
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Seed admin user (password: Password1! - BCrypt hash)
-- Note: This is a BCrypt hash of "Password1!" with strength 10
INSERT INTO account (id, first_name, middle_name, last_name, street, city, state, zip_code, country, phone_number, date_of_birth, email, password, admin) VALUES
    (1, 'Admin', 'Test', 'User', '123 Admin St', 'New York', 'NY', '10001', 'USA', 1234567890, '1990-01-01', 'test@test.com', '$2a$10$8K1p/a0dL3.XVP2xnOWGGeG9A2xNe2aFUBdwTzktFN9TkIfW2hdm6', TRUE)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Seed regular users
INSERT INTO account (id, first_name, middle_name, last_name, street, city, state, zip_code, country, phone_number, date_of_birth, email, password, admin) VALUES
    (2, 'John', 'A', 'Doe', '456 Main St', 'Los Angeles', 'CA', '90001', 'USA', 2345678901, '1985-05-15', 'john.doe@example.com', '$2a$10$8K1p/a0dL3.XVP2xnOWGGeG9A2xNe2aFUBdwTzktFN9TkIfW2hdm6', FALSE),
    (3, 'Jane', 'B', 'Smith', '789 Oak Ave', 'Chicago', 'IL', '60601', 'USA', 3456789012, '1992-08-22', 'jane.smith@example.com', '$2a$10$8K1p/a0dL3.XVP2xnOWGGeG9A2xNe2aFUBdwTzktFN9TkIfW2hdm6', FALSE)
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Seed policies
INSERT INTO policy (id, line_id, account_id, premium, start_date, end_date) VALUES
    (1, 1, 2, 1200.00, '2024-01-01', '2025-01-01'),
    (2, 2, 2, 2500.00, '2024-01-01', '2025-01-01'),
    (3, 3, 3, 5000.00, '2024-06-01', '2025-06-01'),
    (4, 4, 3, 800.00, '2024-01-01', '2025-01-01')
ON DUPLICATE KEY UPDATE line_id=VALUES(line_id);

-- Seed claims
INSERT INTO claim (id, claim_number, description, claim_date, claim_status, policy_id) VALUES
    (1, 'CLM-2024-001', 'Minor fender bender on highway', '2024-03-15', 'APPROVED', 1),
    (2, 'CLM-2024-002', 'Water damage from burst pipe', '2024-04-20', 'IN_PROGRESS', 2),
    (3, 'CLM-2024-003', 'Annual health checkup', '2024-05-10', 'APPROVED', 4)
ON DUPLICATE KEY UPDATE claim_number=VALUES(claim_number);
