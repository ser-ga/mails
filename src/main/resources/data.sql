DELETE
FROM mail;
DELETE
FROM roles;
DELETE
FROM author;

INSERT INTO author (active, full_name, password, username)
VALUES (true, 'Global administrator', '{noop}passw', 'admin'),
       (true, 'Office manager', '{noop}passw', 'mngr'),
       (true, 'Employer', '{noop}passw', 'empl');

INSERT INTO roles (author_id, role)
VALUES (10000, 'ROLE_ADMIN'),
       (10001, 'ROLE_MANAGER'),
       (10002, 'ROLE_USER');

INSERT INTO mail (accept, create_date, number, recipient, subject, text, update_date, version, author_id)
VALUES (true, '2018-12-15 10:00:00', 12, 'recipient1', 'subject1', 'text1', '2018-12-15 10:00:00', 1, 10001),
       (true, '2018-12-15 11:00:00', 13, 'recipient2', 'subject2', 'text2', '2018-12-15 11:00:00', 1, 10002),
       (true, '2018-12-15 12:00:00', 14, 'recipient3', 'subject3', 'text3', '2018-12-15 12:00:00', 1, 10001),
       (true, '2018-12-15 13:00:00', 15, 'recipient2', 'subject4', 'text4', '2018-12-15 13:00:00', 1, 10000),
       (true, '2018-12-15 14:00:00', 16, 'recipient3', 'subject5', 'text5', '2018-12-15 14:00:00', 1, 10001);
