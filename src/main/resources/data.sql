insert into person (id, email, name) values (1, 'lennon@example.com', 'John Lennon');
insert into person (id, email, name) values (2, 'gates@example.com', 'Bill Gates');
insert into person (id, email, name) values (3, 'obama@example.com', 'Barack Obama');
insert into person (id, email, name) values (4, 'sanders@example.com', 'Bernie Sanders');

insert into project(id, description, name) values (5, 'American Dream', 'America');
insert into person_project(person_id, project_id) values (2, 5);
insert into person_project(person_id, project_id) values (3, 5);
insert into person_project(person_id, project_id) values (4, 5);

insert into project (id, description, name) values (6, 'Worldwide Known', 'Worldwide');
insert into person_project(person_id, project_id) values (1, 6);
insert into person_project(person_id, project_id) values (2, 6);

insert into task(id, description, name, person_fk, project_fk) values (7,
 'Becoming an American Presidential Candidate', 'Democratic Candidate', 4, 5);

insert into task (id, description, name, person_fk, project_fk) values (8,
'Fulfilling the American Dream', 'American Dream', 2, 5);

insert into task (id, description, name, person_fk, project_fk) values (9,
'First African American President', 'Black Dream', 3, 5);

insert into project (id, description, name) values (10, 'Be a Legend', 'Legend');

insert into person_project (person_id, project_id) values (1, 10);
insert into person_project (person_id, project_id) values (3, 10);

insert into task (id, description, name, person_fk, project_fk) values (11,
'Be The Best Musician', 'The Beatles', 1, 10);

insert into task (id, description, name, person_fk, project_fk) values (12,
'Get Nobel Peace Prize', 'Nobel', 3, 10);

alter sequence hibernate_sequence restart with 13;