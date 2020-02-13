
DROP TABLE IF EXISTS mail;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS author;

DROP SEQUENCE IF EXISTS GLOBAL_SEQ;

CREATE SEQUENCE GLOBAL_SEQ START 10000;

CREATE TABLE author
(
  ID         bigint default GLOBAL_SEQ.nextval primary key,
  ACTIVE     BOOLEAN DEFAULT TRUE    NOT NULL,
  FULL_NAME  VARCHAR(255)            NOT NULL,
  USERNAME   VARCHAR(255)            NOT NULL,
  PASSWORD   VARCHAR(255)            NOT NULL,
  PHONE      VARCHAR(255)            NOT NULL,
  REGISTERED TIMESTAMP DEFAULT now() NOT NULL,
  SIGNATORY BOOLEAN DEFAULT FALSE NOT NULL
);
CREATE UNIQUE INDEX username_idx
  ON author (USERNAME);

CREATE TABLE roles
(
  AUTHOR_ID bigint NOT NULL,
  ROLE      VARCHAR(255) NOT NULL,
  CONSTRAINT roles_idx UNIQUE (AUTHOR_ID, ROLE),
  FOREIGN KEY (AUTHOR_ID) REFERENCES author (ID)
    ON DELETE CASCADE
);

CREATE TABLE mail
(
  ID          bigint default GLOBAL_SEQ.nextval primary key,
  ACCEPT      BOOLEAN DEFAULT FALSE        NOT NULL,
  CREATE_DATE DATE                         NOT NULL,
  CREATE_YEAR INT                         NOT NULL,
  NUMBER      INT                          NOT NULL,
  RECIPIENT   VARCHAR(255)                 NOT NULL,
  SUBJECT     VARCHAR(255)                 NOT NULL,
  TITLE       VARCHAR(255),
  TEXT        text                         NOT NULL,
  UPDATE_DATE DATETIME,
  VERSION     INT                          NOT NULL,
  AUTHOR_ID   BIGINT                       NOT NULL,
  SIGNATORY_ID   BIGINT                       NOT NULL,
  FOREIGN KEY (AUTHOR_ID) REFERENCES author (ID)
    ON DELETE NO ACTION
);
CREATE UNIQUE INDEX mail_idx
  ON mail (CREATE_YEAR, NUMBER);
