drop table if exists Employee;
create table Employee(
    id int not null auto_increment,
    name varchar(50) not null,
    joining_date date not null,
    salary double not null,
    ssn varchar(30) not null unique,
    primary key (id)
);

drop table if exists User;
create table User(
    id int not null auto_increment,
    username varchar(50) not null,
    password varchar(50) not null,
    primary key (id)
);

drop table if exists Media;
create table Media(
    id int not null auto_increment,
    type varchar(10) not null,
    title varchar(255) not null,
    filename varchar(255) not null,
    primary key (id)
);

drop table if exists CrawledFile;
create table CrawledFile(
    id int not null auto_increment,
    filename varchar(255) not null,
    lastIndexed date not null,
    primary key (id)
);

drop table if exists users;
CREATE  TABLE users (
    username VARCHAR(45) NOT NULL ,
    password VARCHAR(45) NOT NULL ,
    enabled TINYINT NOT NULL DEFAULT 1 ,
    PRIMARY KEY (username))
;

drop table if exists user_roles;
CREATE TABLE user_roles (
    user_role_id int(11) NOT NULL AUTO_INCREMENT,
    username varchar(45) NOT NULL,
    role varchar(45) NOT NULL,
    PRIMARY KEY (user_role_id),
    UNIQUE KEY uni_username_role (role,username),
    KEY fk_username_idx (username),
    CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username)
);
