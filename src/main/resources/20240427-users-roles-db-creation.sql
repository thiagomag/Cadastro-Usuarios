CREATE TABLE users_roles
(
    user_id bigint NOT NULL,
    role_id bigint NOT NULL,

    CONSTRAINT FK_Users FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FK_Roles FOREIGN KEY (role_id) REFERENCES roles(id)
);

DROP TABLE users_roles;


