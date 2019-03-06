-- create table horse if not exists
DROP TABLE horse;
DROP SEQUENCE seq_1;

CREATE SEQUENCE seq_1
START WITH 1
INCREMENT BY 1
MAXVALUE 999
CYCLE;


CREATE TABLE IF NOT EXISTS horse (
  -- use auto incrementing IDs as primary key
  --id        BIGINT AUTO_INCREMENT PRIMARY KEY,
  id        BIGINT DEFAULT seq_1.nextval PRIMARY KEY,
  name      VARCHAR(255) NOT NULL,
  breed     TEXT,
  min_speed DOUBLE       NOT NULL,
  max_speed DOUBLE       NOT NULL,
  created   DATETIME     NOT NULL,
  updated   DATETIME     NOT NULL
);
