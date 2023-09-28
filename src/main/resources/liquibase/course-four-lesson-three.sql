--liquibase formatted sql
--changeset volynkina:1
CREATE INDEX IDX_students_name ON students(name);

--changeset volynkina:2
CREATE INDEX IDX_faculties_name_colour ON faculties(name,colour);