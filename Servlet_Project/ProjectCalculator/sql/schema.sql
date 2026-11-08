-- Run this file once in MySQL Workbench (or: mysql -u root -p < schema.sql)
CREATE DATABASE IF NOT EXISTS project_calculator CHARACTER SET utf8mb4;
USE project_calculator;

DROP TABLE IF EXISTS plan_items;
DROP TABLE IF EXISTS plans;
DROP TABLE IF EXISTS tasks;

-- Work to be done. "hours" is the estimated effort; the calculator spreads
-- the tasks over the working days between the start date and the end date.
CREATE TABLE tasks (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  phase      VARCHAR(60)  NOT NULL,
  name       VARCHAR(120) NOT NULL,
  hours      DECIMAL(6,1) NOT NULL,
  sort_order INT          NOT NULL
);

-- One row per saved calculation
CREATE TABLE plans (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  start_date      DATE         NOT NULL,
  end_date        DATE         NOT NULL,
  off_days        VARCHAR(80)  NOT NULL DEFAULT '',
  holidays        VARCHAR(500) NOT NULL DEFAULT '',
  total_work_days INT          NOT NULL,
  total_hours     DECIMAL(8,1) NOT NULL,
  created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

-- Snapshot of the schedule of a saved plan
CREATE TABLE plan_items (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  plan_id    INT          NOT NULL,
  task_name  VARCHAR(120) NOT NULL,
  phase      VARCHAR(60)  NOT NULL,
  start_date DATE         NOT NULL,
  end_date   DATE         NOT NULL,
  work_days  INT          NOT NULL,
  hours      DECIMAL(6,1) NOT NULL,
  FOREIGN KEY (plan_id) REFERENCES plans(id) ON DELETE CASCADE
);

INSERT INTO tasks (phase, name, hours, sort_order) VALUES
('Planning', 'Requirement analysis',               8, 1),
('Planning', 'Proposal and feasibility',           6, 2),
('Design',   'Database design and ERD',            6, 3),
('Design',   'UML diagrams and UI mockup',         8, 4),
('Setup',    'IntelliJ, Maven and Tomcat setup',   4, 5),
('Setup',    'MySQL schema and sample data',       4, 6),
('Backend',  'Model classes and DB connection',    6, 7),
('Backend',  'DAO layer (JDBC)',                  10, 8),
('Backend',  'Scheduler logic (calculator)',      16, 9),
('Backend',  'Servlets and JSON API',             10, 10),
('Frontend', 'HTML and CSS layout',               10, 11),
('Frontend', 'Gantt chart',                       16, 12),
('Frontend', 'JavaScript and API integration',     8, 13),
('Testing',  'Unit tests',                         8, 14),
('Testing',  'System testing and bug fixing',     12, 15),
('Report',   'Final report',                      12, 16),
('Report',   'Slides and demo',                    8, 17);
