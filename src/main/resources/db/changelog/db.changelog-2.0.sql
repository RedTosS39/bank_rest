--liquibase formatted sql

--changeset dlevazhinskiy:1
INSERT INTO users (username, role, user_password)
VALUES ('testUser1', 'ADMIN', '123456')
ON CONFLICT (username) DO NOTHING;



