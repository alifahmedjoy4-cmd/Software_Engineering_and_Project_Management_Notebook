-- Runs at every start. Safe to repeat: tables are only created when missing.
CREATE TABLE IF NOT EXISTS tasks (
  id         INT AUTO_INCREMENT PRIMARY KEY,
  phase      VARCHAR(60)  NOT NULL,
  name       VARCHAR(120) NOT NULL,
  hours      DECIMAL(6,1) NOT NULL,
  sort_order INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS plans (
  id              INT AUTO_INCREMENT PRIMARY KEY,
  start_date      DATE         NOT NULL,
  end_date        DATE         NOT NULL,
  off_days        VARCHAR(80)  NOT NULL DEFAULT '',
  holidays        VARCHAR(500) NOT NULL DEFAULT '',
  total_work_days INT          NOT NULL,
  total_hours     DECIMAL(8,1) NOT NULL,
  created_at      TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS plan_items (
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
