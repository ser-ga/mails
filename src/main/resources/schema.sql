-- HSQLDB

DROP TABLE mail
IF EXISTS;
DROP TABLE roles
IF EXISTS;
DROP TABLE author
IF EXISTS;

DROP SEQUENCE global_seq
IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ
  AS INTEGER
  START WITH 10000;

CREATE TABLE author
(
  ID         BIGINT GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  ACTIVE     BOOLEAN DEFAULT TRUE    NOT NULL,
  FULL_NAME  VARCHAR(255)            NOT NULL,
  USERNAME   VARCHAR(255)            NOT NULL,
  PASSWORD   VARCHAR(255)            NOT NULL,
  REGISTERED TIMESTAMP DEFAULT now() NOT NULL

);
CREATE UNIQUE INDEX username_idx
  ON author (USERNAME);

CREATE TABLE roles
(
  AUTHOR_ID INTEGER NOT NULL,
  ROLE      VARCHAR(255),
  CONSTRAINT roles_idx UNIQUE (AUTHOR_ID, ROLE),
  FOREIGN KEY (AUTHOR_ID) REFERENCES author (ID)
    ON DELETE CASCADE
);

CREATE TABLE mail
(
  ID          BIGINT GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  ACCEPT      BOOLEAN DEFAULT FALSE        NOT NULL,
  CREATE_DATE DATE                         NOT NULL,
  NUMBER      INT                          NOT NULL,
  RECIPIENT   VARCHAR(255)                 NOT NULL,
  SUBJECT     VARCHAR(255)                 NOT NULL,
  TITLE       VARCHAR(255),
  TEXT        VARCHAR(2000)                NOT NULL,
  UPDATE_DATE DATETIME DEFAULT now()       NOT NULL,
  VERSION     INT                          NOT NULL,
  AUTHOR_ID   BIGINT                       NOT NULL,
  FOREIGN KEY (AUTHOR_ID) REFERENCES author (ID)
    ON DELETE NO ACTION
);
CREATE UNIQUE INDEX mail_idx
  ON mail (NUMBER);
