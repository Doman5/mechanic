--liquibase formatted sql
--changeset ddomanski:5;

create table users(
    id bigint not null auto_increment PRIMARY KEY,
    username varchar(50) not null unique,
    password varchar(500) not null,
    enabled boolean not null
);

create table authorities (
    username varchar(50) not null,
    authority varchar(50) not null,
    constraint fk_authorities_username foreign key(username) references users(username)
);

insert into users (id ,username, password, enabled)
values (1, 'mechanic', '{bcrypt}$2a$10$upzXFsFUOClFRR69OMKF8eajGMRs0vhcSHqvNDKy9yfW45w7o9z6O', true);
insert into authorities (username, authority) values ('mechanic','ROLE_MECHANIC');