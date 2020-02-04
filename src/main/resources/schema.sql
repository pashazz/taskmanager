drop table task if exists;
drop table person_project if exists;
drop table person if exists;
drop table project if exists;

drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
create table person (id bigint not null, email varchar(255), name varchar(255), primary key (id));
create table person_project (person_id bigint not null, project_id bigint not null);
create table project (id bigint not null, description varchar(255), name varchar(255), primary key (id));
create table task (id bigint not null, description varchar(255), name varchar(255) not null, person_fk bigint, project_fk bigint not null, primary key (id));
alter table person_project add constraint project_id_valid foreign key (project_id) references project on delete cascade ;
alter table person_project add constraint person_id_valid foreign key (person_id) references person on delete cascade ;
alter table task add constraint task_person_valid foreign key (person_fk) references person on delete set null;
alter table task add constraint task_project_valid foreign key (project_fk) references project on delete cascade;
alter table task add constraint onmyprojects check ((PERSON_FK is NULL) or (PERSON_FK in (select distinct PERSON_ID from PERSON_PROJECT where PROJECT_ID=PROJECT_FK)));
create trigger on_task_project_change before delete on person_project for each row call "io.github.pashazz.taskmanager.trigger.UnassignTaskOnRemovePersonFromProject";
