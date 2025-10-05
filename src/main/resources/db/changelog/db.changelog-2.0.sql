--liquibase formatted sql

--changeset dlevazhinskiy:1
INSERT INTO users (card_owner, role, user_password)
VALUES ('testUser1', 'ADMIN', '123456');
--changeset dlevazhinskiy:2
DELETE FROM users WHERE user_id = 1;



