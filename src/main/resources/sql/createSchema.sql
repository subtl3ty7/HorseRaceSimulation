-- create table horse if not exists
DROP TABLE IF EXISTS horse;
DROP TABLE IF EXISTS jockey;
CREATE TABLE IF NOT EXISTS horse(
  -- use auto incrementing IDs as primary key
  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  breed     TEXT,
  min_speed DOUBLE       NOT NULL,
  max_speed DOUBLE       NOT NULL,
  created   DATETIME     NOT NULL,
  updated   DATETIME     NOT NULL
);

CREATE TABLE IF NOT EXISTS jockey (

  id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  skill     DOUBLE       NOT NULL,
  created   DATETIME     NOT NULL,
  updated   DATETIME     NOT NULL

);


CREATE TABLE IF NOT EXISTS simulation (

  id          BIGINT       NOT NULL,
  name        VARCHAR(255) NOT NULL,
  created     DATETIME     NOT NULL,
  horseName   VARCHAR(255) NOT NULL,
  jockeyName  VARCHAR(255) NOT NULL,
  avgSpeed    DOUBLE       NOT NULL,
  horseSpeed  DOUBLE       NOT NULL,
  skill       DOUBLE       NOT NULL,
  luckFactor  DOUBLE       NOT NULL,
  rank        BIGINT       NOT NULL

);


