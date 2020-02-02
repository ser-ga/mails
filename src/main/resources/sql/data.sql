
INSERT INTO author ( active, full_name, password, username, phone)
VALUES (true, 'Global administrator', '{noop}admin', 'admin', '89001234567'),
       (true, 'Office manager', '{noop}mngr', 'mngr', '89001234567'),
       (true, 'Employer', '{noop}empl', 'empl', '89001234567');

INSERT INTO roles (author_id, role)
VALUES (10000, 'ROLE_ADMIN'),
       (10001, 'ROLE_MANAGER'),
       (10002, 'ROLE_USER');
