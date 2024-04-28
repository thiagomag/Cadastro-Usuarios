CREATE TABLE roles
(
    id                   serial       PRIMARY KEY NOT NULL,
    name             varchar(255) UNIQUE NOT NULL
);

INSERT INTO public.roles(name) VALUES('ROLE_CUSTOMER'), ('ROLE_ADMINISTRATOR');

DROP TABLE roles;