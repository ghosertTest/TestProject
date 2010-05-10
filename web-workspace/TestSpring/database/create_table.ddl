CREATE TABLE users
(
    id INT auto_increment,
    username VARCHAR(50),
    password VARCHAR(50),
    CONSTRAINT users_id_pk PRIMARY KEY (id)
)