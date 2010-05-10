drop database if exists Assistant;
create database Assistant;
use Assistant;
create table users
(
    user_id int unsigned auto_increment not null,
    username varchar(15) not null,
    password varchar(15) not null,
    name varchar(15) not null,
    email varchar(40),
    mobile varchar(25),
    registertime datetime not null,
    ip varchar(15),
    primary key(user_id, username)
);
create table suggestions
(
    sug_id int unsigned auto_increment not null primary key,
    sugname varchar(15),
    sugemail varchar(40),
    sugtime datetime not null,
    sugip varchar(15),
    suggestion text not null
);