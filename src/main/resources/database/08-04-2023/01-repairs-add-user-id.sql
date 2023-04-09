--liquibase formatted sql
--changeset ddomanski:3;

alter table repairs add column user_id bigint;