--liquibase formatted sql
--changeset dlevazhinskiy:1
DELETE FROM  users  WHERE username = 'user';
-- --changeset dlevazhinskiy:2
-- DELETE FROM  users  WHERE username = 'test_user_1';