delete from user_role;
delete from users;

insert into users(id, username, email, password, enabled) values
(1, 'Librarian', 'librarian@lib.com', 'qwerty', 1),
(2, 'SomeUser', 'someuser@lib.com', 'qwerty', 1);

insert into user_role(user_id, roles) values
(1, 'USER'),
(2, 'USER');
