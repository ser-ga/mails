INSERT INTO author ( active, full_name, password, username, phone, signatory)
VALUES (true, 'Global administrator', '{noop}passw', 'admin', '89001234567', false),
       (true, 'Office manager', '{noop}passw', 'mngr', '89001234567', true),
       (true, 'Employer', '{noop}passw', 'empl', '89001234567', false);

INSERT INTO roles (author_id, role)
VALUES (10000, 'ROLE_ADMIN'),
       (10001, 'ROLE_MANAGER'),
       (10002, 'ROLE_USER');

INSERT INTO mail (accept, create_date, create_year, number, recipient, subject, title, text, update_date, version, author_id, signatory_id)
VALUES (true, '2018-12-15', 2018,  12, 'recipient1', 'subject1', 'title1','text1', '2018-12-15 10:00:00', 1, 10001, 10001),
       (true, '2018-12-15', 2018, 13, 'recipient2', 'subject2', 'title2','text2', '2018-12-15 11:00:00', 1, 10002, 10001),
       (true, '2018-12-15', 2018, 14, 'recipient3', 'subject3', 'title3','text3', '2018-12-15 12:00:00', 1, 10001, 10001),
       (true, '2018-12-15', 2018, 15, 'recipient2', 'subject4', 'title4','text4', '2018-12-15 13:00:00', 1, 10000, 10001),
       (true, '2018-12-15', 2018, 16, 'recipient3', 'subject5', 'title5','text5', '2018-12-15 14:00:00', 1, 10001, 10001);