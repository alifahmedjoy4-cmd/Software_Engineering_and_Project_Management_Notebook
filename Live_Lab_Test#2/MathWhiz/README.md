# ALIF AHMED JOY – MATH WHIZ

A web-based mathematical game built with **Java Servlet, JSP and JDBC (MySQL)**.
Players enter their name, answer four questions (prime, modulus, two trigonometry), see their score, and the best scores appear on a Top 10 leaderboard.

**Author:** Alif Ahmed Joy

## Project Objectives
- Build reusable Java methods for mathematical calculations.
- Calculate every answer in Java (no manually stored answers).
- Handle requests/responses with a Servlet and JSP pages.
- Save and read data with JDBC `PreparedStatement`.
- Document and submit a complete project on GitHub.

## Technologies Used
Java 17, Jakarta Servlet 6 / JSP, JDBC, MySQL 8, Apache Tomcat 10.1, Maven, HTML/CSS, JUnit 5.

## Software Requirements
- JDK 17 or newer
- Apache Tomcat **10.1+** (uses the `jakarta.*` namespace)
- MySQL Server 8+
- Maven 3.8+ (or IntelliJ IDEA's bundled Maven)

## Database Configuration
1. Run the SQL below (also in `database/math_whiz_db.sql`).
2. Edit `src/main/resources/db.properties` and set your MySQL user and password.

```sql
CREATE DATABASE IF NOT EXISTS math_whiz_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE math_whiz_db;

CREATE TABLE IF NOT EXISTS game_scores (
    id          INT           NOT NULL AUTO_INCREMENT PRIMARY KEY,
    player_name VARCHAR(100)  NOT NULL,
    score       INT           NOT NULL,
    difficulty  VARCHAR(20)   NOT NULL DEFAULT 'NORMAL',
    played_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

## Steps to Run
**IntelliJ IDEA**
1. `File > Open` and select the `MathWhiz` folder (open as Maven project).
2. `Run > Edit Configurations > + > Tomcat Server > Local`, choose your Tomcat 10.1 folder.
3. On the *Deployment* tab add `MathWhiz:war exploded`; set the application context to `/MathWhiz`.
4. Run, then open `http://localhost:8080/MathWhiz/`.

**Command line**
```bash
mvn clean package          # runs the unit tests and builds target/MathWhiz.war
# copy target/MathWhiz.war into Tomcat's webapps folder, start Tomcat, then open
# http://localhost:8080/MathWhiz/
```

## Game Rules and Scoring
| Question | Area | Marks |
|---|---|---|
| Q1 | Prime Number (1 = prime, 0 = not prime) | 2 |
| Q2 | Modulus (integers only, divisor never zero) | 2 |
| Q3 | Trigonometry (sin / cos / tan, degrees) | 3 |
| Q4 | Trigonometry (a different function) | 3 |
| **Total** | | **10** |

- Difficulty is `NORMAL` and is stored with every score.
- Trigonometric answers use degrees, converted with `Math.toRadians`, and are compared with a tolerance of 0.01 (answer rounded to 2 decimals).
- Undefined tangents (90°, 270°) are never asked; `calculateTan` returns `NaN` for them.
- Leaderboard: `ORDER BY score DESC, played_at ASC LIMIT 10`.

## Project Structure
```
MathWhiz/
├── src/main/java/
│   ├── controller/GameServlet.java
│   ├── model/ (GameScore, Question, QuestionResult)
│   ├── service/MathService.java
│   ├── dao/ (ScoreDAO, DBConnection)
│   └── util/Html.java
├── src/main/resources/db.properties
├── src/main/webapp/ (index.jsp, game.jsp, result.jsp, leaderboard.jsp, css/style.css)
├── src/test/java/service/MathServiceTest.java
├── database/math_whiz_db.sql
└── README.md
```

## Application Screenshots
*(Run the app, take the screenshots and save them in the `screenshots/` folder with these names.)*

### Welcome Page
![Welcome Page](screenshots/home.png)

### Game Page
![Game Page](screenshots/game.png)

### Result Page
![Result Page](screenshots/result.png)

### Leaderboard
![Leaderboard](screenshots/leaderboard.png)

## GitHub Project Directory Link
https://github.com/YOUR-USERNAME/YOUR-REPOSITORY/tree/main/MathWhiz
