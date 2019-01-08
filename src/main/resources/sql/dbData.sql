DELETE
FROM author;
DELETE
FROM roles;

INSERT INTO author (active, full_name, password, username)
VALUES  (true, 'Global administrator', '{noop}passw', 'admin'),
        (true, 'Office manager', '{noop}passw', 'mngr');

INSERT INTO roles (author_id, role)
VALUES  (1, 'ROLE_ADMIN'),
        (2, 'ROLE_MANAGER');