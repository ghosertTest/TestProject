CREATE TABLE T_User
(
    id INT auto_increment,
    name VARCHAR(50) NOT NULL,
    age INT NOT NULL,
    group_id INT NOT NULL,
    version INT NOT NULL,
    CONSTRAINT T_User_id_pk PRIMARY KEY(id)
);

CREATE TABLE T_Passport
(
    id INT auto_increment,
    serial VARCHAR(30) NOT NULL,
    expiry INT NOT NULL,
    CONSTRAINT T_Passport_id_pk PRIMARY KEY(id)
);

CREATE TABLE T_Group
(
    id INT auto_increment,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT T_Group_id_pk PRIMARY KEY(id)
);

CREATE TABLE T_Address
(
    id INT auto_increment,
    address VARCHAR(200) NOT NULL,
    zipcode VARCHAR(10),
    tel VARCHAR(20),
    user_id int NOT NULL,
    idx int,
    CONSTRAINT T_Address_id_pk PRIMARY KEY(id)
);

ALTER TABLE T_User
ADD CONSTRAINT user_group_fk
    FOREIGN KEY (group_id)
    REFERENCES T_Group(id);

ALTER TABLE T_Passport
ADD CONSTRAINT passport_user_fk
    FOREIGN KEY (id)
    REFERENCES T_User(id);

ALTER TABLE T_Address
ADD CONSTRAINT address_user_fk
    FOREIGN KEY (user_id)
    REFERENCES T_User(id);