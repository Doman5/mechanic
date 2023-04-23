--liquibase formatted sql
--changeset ddomanski:5

create table users(
    id bigint not null auto_increment PRIMARY KEY,
    username varchar(50) not null unique,
    password varchar(500) not null,
    role varchar(100) not null
);

--liquibase formatted sql
--changeset ddomanski:6


insert into users (id ,username, password, role)
values (1, 'mechanic', '$2a$10$W2Sw8mIXOtliD5ZeBuezP.IJaIe7Fk7GwriMbxnV/XNvdKCof7Y7i', 'ROLE_MECHANIC');