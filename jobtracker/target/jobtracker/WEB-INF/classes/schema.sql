CREATE DATABASE IF NOT EXISTS jobtracker_db
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE jobtracker_db;

CREATE TABLE IF NOT EXISTS users (
  user_id     INT AUTO_INCREMENT PRIMARY KEY,
  full_name   VARCHAR(100) NOT NULL,
  email       VARCHAR(150) NOT NULL UNIQUE,
  password    VARCHAR(100) NOT NULL,
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS job_applications (
  application_id    INT AUTO_INCREMENT PRIMARY KEY,
  user_id           INT NOT NULL,
  company_name      VARCHAR(150) NOT NULL,
  job_role          VARCHAR(150) NOT NULL,
  location          VARCHAR(150),
  job_type          VARCHAR(30),
  application_date  DATE NOT NULL,
  job_description   TEXT,
  job_url           VARCHAR(500),
  status            ENUM('Applied','Assessment','Interview','Selected','Rejected','Offer Received')
                    NOT NULL DEFAULT 'Applied',
  created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_app_user FOREIGN KEY (user_id)
    REFERENCES users(user_id) ON DELETE CASCADE,
  INDEX idx_user_status (user_id, status),
  INDEX idx_user_date (user_id, application_date)
);