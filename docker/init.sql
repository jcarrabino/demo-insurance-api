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
    password      VARCHAR(255)
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

-- Seed data
INSERT IGNORE INTO line (id, name, description, max_coverage, min_coverage) VALUES
    (1, 'Auto',     'Automobile insurance coverage',  500000, 10000),
    (2, 'Home',     'Homeowners insurance coverage',  1000000, 50000),
    (3, 'Life',     'Life insurance coverage',        2000000, 100000),
    (4, 'Health',   'Health insurance coverage',      500000, 5000);
