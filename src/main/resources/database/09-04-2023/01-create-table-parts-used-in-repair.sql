--liquibase formatted sql
--changeset ddomanski:4;

create table repairs_parts(
    id bigint primary key not null auto_increment,
    repair_id bigint not null,
    part_id bigint not null,
    quantity bigint not null,
    constraint fk_repairs_parts_repair_id foreign key (repair_id) references repairs(id),
    constraint fk_repairs_parts_part_id foreign key (part_id) references parts(id)
)