-- Drop existing tables to ensure fresh data
DROP TABLE IF EXISTS claim;
DROP TABLE IF EXISTS account_policy;
DROP TABLE IF EXISTS policy;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS line;

-- Create tables
CREATE TABLE line (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    max_coverage FLOAT,
    min_coverage FLOAT
);

CREATE TABLE account (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(255) NOT NULL,
    middle_name   VARCHAR(255),
    last_name     VARCHAR(255) NOT NULL,
    street        VARCHAR(255),
    city          VARCHAR(255),
    state         VARCHAR(255),
    zip_code      VARCHAR(255),
    country       VARCHAR(255),
    phone_number  VARCHAR(20),
    date_of_birth DATE,
    email         VARCHAR(255) UNIQUE NOT NULL,
    password      VARCHAR(255) NOT NULL,
    admin         BOOLEAN DEFAULT FALSE
);

CREATE TABLE policy (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    line_id    INT NOT NULL,
    account_id INT NOT NULL,
    premium    DECIMAL(19,2) NOT NULL,
    start_date DATE NOT NULL,
    end_date   DATE NOT NULL,
    CONSTRAINT fk_policy_line    FOREIGN KEY (line_id)    REFERENCES line(id) ON DELETE CASCADE,
    CONSTRAINT fk_policy_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE
);

CREATE TABLE account_policy (
    account_id INT NOT NULL,
    policy_id  INT NOT NULL,
    PRIMARY KEY (account_id, policy_id),
    CONSTRAINT fk_ap_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    CONSTRAINT fk_ap_policy  FOREIGN KEY (policy_id)  REFERENCES policy(id) ON DELETE CASCADE
);

CREATE TABLE claim (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    claim_number VARCHAR(255) NOT NULL,
    description  VARCHAR(255),
    claim_date   DATE NOT NULL,
    claim_status VARCHAR(50) NOT NULL,
    policy_id    INT NOT NULL,
    CONSTRAINT fk_claim_policy FOREIGN KEY (policy_id) REFERENCES policy(id) ON DELETE CASCADE
);

-- Seed data for lines
INSERT INTO line (name, description, max_coverage, min_coverage) VALUES
    ('Auto',     'Automobile insurance coverage',  500000, 10000),
    ('Home',     'Homeowners insurance coverage',  1000000, 50000),
    ('Life',     'Life insurance coverage',        2000000, 100000),
    ('Health',   'Health insurance coverage',      500000, 5000);

-- Seed admin user (password: Password1!)
-- BCrypt hash: $2a$10$6B.Ga3IPj/Ahk6CEi0ZBBeJtBK3xbNFOBNYgQpbn6j8H8UGuv5XQa
INSERT INTO account (first_name, middle_name, last_name, street, city, state, zip_code, country, phone_number, date_of_birth, email, password, admin) VALUES
    ('Admin', 'Test', 'User', '123 Admin St', 'New York', 'NY', '10001', 'USA', '5551234567', '1990-01-01', 'test@test.com', '$2a$10$6B.Ga3IPj/Ahk6CEi0ZBBeJtBK3xbNFOBNYgQpbn6j8H8UGuv5XQa', TRUE);

-- Seed regular users (all passwords: Password1!)
INSERT INTO account (first_name, middle_name, last_name, street, city, state, zip_code, country, phone_number, date_of_birth, email, password, admin) VALUES
    ('John', 'A', 'Doe', '456 Main St', 'Los Angeles', 'CA', '90001', 'USA', '5552345678', '1985-05-15', 'john.doe@example.com', '$2a$10$6B.Ga3IPj/Ahk6CEi0ZBBeJtBK3xbNFOBNYgQpbn6j8H8UGuv5XQa', FALSE),
    ('Jane', 'B', 'Smith', '789 Oak Ave', 'Chicago', 'IL', '60601', 'USA', '5553456789', '1992-08-22', 'jane.smith@example.com', '$2a$10$6B.Ga3IPj/Ahk6CEi0ZBBeJtBK3xbNFOBNYgQpbn6j8H8UGuv5XQa', FALSE),
    ('Bob', 'C', 'Johnson', '321 Pine Rd', 'Houston', 'TX', '77001', 'USA', '5554567890', '1988-11-30', 'bob.johnson@example.com', '$2a$10$6B.Ga3IPj/Ahk6CEi0ZBBeJtBK3xbNFOBNYgQpbn6j8H8UGuv5XQa', FALSE);

-- Seed policies (2-3 per user)
INSERT INTO policy (line_id, account_id, premium, start_date, end_date) VALUES
    -- Admin user policies (account_id=1)
    (1, 1, 1500.00, '2024-01-01', '2025-01-01'),
    (2, 1, 3000.00, '2024-01-01', '2025-01-01'),
    (4, 1, 900.00, '2024-01-01', '2025-01-01'),
    -- John Doe policies (account_id=2)
    (1, 2, 1200.00, '2024-01-01', '2025-01-01'),
    (2, 2, 2500.00, '2024-01-01', '2025-01-01'),
    (3, 2, 8000.00, '2024-06-01', '2025-06-01'),
    -- Jane Smith policies (account_id=3)
    (3, 3, 5000.00, '2024-06-01', '2025-06-01'),
    (4, 3, 800.00, '2024-01-01', '2025-01-01'),
    -- Bob Johnson policies (account_id=4)
    (1, 4, 1400.00, '2024-02-01', '2025-02-01'),
    (2, 4, 2800.00, '2024-02-01', '2025-02-01'),
    (4, 4, 850.00, '2024-02-01', '2025-02-01');

-- Seed claims (1-2 per user)
INSERT INTO claim (claim_number, description, claim_date, claim_status, policy_id) VALUES
    -- Admin user claims (policies 1,2,3)
    ('CLM-2024-001', 'Minor fender bender on highway', '2024-03-15', 'APPROVED', 1),
    ('CLM-2024-002', 'Water damage from burst pipe', '2024-04-20', 'IN_PROGRESS', 2),
    -- John Doe claims (policies 4,5,6)
    ('CLM-2024-003', 'Rear-end collision at intersection', '2024-05-10', 'APPROVED', 4),
    ('CLM-2024-004', 'Roof damage from hailstorm', '2024-06-15', 'SUBMITTED', 5),
    -- Jane Smith claims (policies 7,8)
    ('CLM-2024-005', 'Annual health checkup', '2024-07-01', 'APPROVED', 8),
    -- Bob Johnson claims (policies 9,10,11)
    ('CLM-2024-006', 'Windshield crack from road debris', '2024-08-05', 'IN_PROGRESS', 9),
    ('CLM-2024-007', 'Emergency room visit', '2024-09-10', 'APPROVED', 11);
