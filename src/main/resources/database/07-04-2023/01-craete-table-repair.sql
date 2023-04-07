--liquibase formatted sql
--changeset ddomanski:1

create table repairs(
    id bigint primary key not null auto_increment,
    description varchar(500) not null,
    date date,
    work_time double(3,1),
    repair_cost decimal(9,2),
    repair_status varchar(255) not null
);