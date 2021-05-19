INSERT INTO users (name, email, password)
VALUES ('admin', 'admin@mail.ru', '{noop}admin'),
       ('user', 'user@mail.ru', '{noop}user');

INSERT INTO user_roles (user_id, role)
VALUES (1, 'ADMIN'),
       (2, 'USER');

INSERT INTO restaurants (name)
VALUES ('One restaurant'),
       ('Two restaurant'),
       ('Three restaurant'),
       ('Four restaurant'),
       ('Five restaurant');
INSERT INTO dishes (restaurant_id, name, price, date)
VALUES (1, 'Baked potato', 1, '2021-04-10'),
       (1, 'Burger', 2, '2021-04-10'),
       (1, 'Casserole', 3, '2021-04-10'),
       (1, 'Chicken salad', 4, '2021-04-11'),
       (1, 'Crumble', 5, '2021-04-11'),
       (1, 'Curry', 6, '2021-04-11'),
       (2, 'Onion Rings', 1, '2021-04-10'),
       (2, 'Mozzarella Sticks', 2, '2021-04-10'),
       (2, 'Reuben', 3, '2021-04-10'),
       (2, 'Shrimp', 4, '2021-04-11'),
       (2, 'Calamari', 5, '2021-04-11'),
       (2, 'Arancini', 6, '2021-04-11'),
       (4, 'Greek Gyro', 1, now()),
       (4, 'Buffalo Chicken', 2, now()),
       (4, 'Roast Beef', 3, now()),
       (5, 'Ham Slider', 3, now()),
       (5, 'Pizza Slider', 2, now()),
       (5, 'Potato Cakes', 1, now());

INSERT INTO votes (date, user_id, restaurant_id)
VALUES ('2021-04-11', 1, 3),
       ('2021-04-11', 2, 3),
       ('2021-04-12', 1, 4),
       ('2021-04-12', 2, 4),
       (now(), 1, 3),
       (now(), 2, 4);
