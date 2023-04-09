--liquibase formatted sql
--changeset ddomanski:2

create table parts(
    id bigint primary key not null auto_increment,
    name varchar(255),
    price decimal(9,2)
);