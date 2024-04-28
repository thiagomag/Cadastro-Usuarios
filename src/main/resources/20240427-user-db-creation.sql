CREATE TABLE users
(
    id serial PRIMARY KEY NOT NULL,
    email varchar(255) UNIQUE NOT NULL,
    password varchar(100) NOT NULL
);

DROP TABLE users;