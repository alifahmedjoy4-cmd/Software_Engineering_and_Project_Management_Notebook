-- MATH WHIZ database setup (MySQL 8+)
CREATE DATABASE IF NOT EXISTS math_whiz_db
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE math_whiz_db;

CREATE TABLE IF NOT EXISTS game_scores (
    id          INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100)  NOT NULL,
    score       INT           NOT NULL,
    difficulty  VARCHAR(20)   NOT NULL DEFAULT 'NORMAL',
    played_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Top 10 query used by the leaderboard
-- SELECT * FROM game_scores ORDER BY score DESC, played_at ASC LIMIT 10;
