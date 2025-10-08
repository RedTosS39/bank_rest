--liquibase formatted sql
--changeset dlevazhinskiy:1
ALTER TABLE bank_card ADD COLUMN version INT NOT NULL DEFAULT 0;