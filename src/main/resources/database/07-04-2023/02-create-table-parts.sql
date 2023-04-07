--liquibase formatted sql
--changeset ddomanski:2

create table parts(
    id bigint primary key not null auto_increment,
    name varchar(255),
    count bigint not null,
    price decimal(9,2),
    repair_id bigint,
    foreign key fk_parts_repair_id (repair_id) references repairs(id)
);