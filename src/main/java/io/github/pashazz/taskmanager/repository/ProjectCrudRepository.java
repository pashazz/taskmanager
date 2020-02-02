package io.github.pashazz.taskmanager.repository;

import io.github.pashazz.taskmanager.entity.Project;
import org.springframework.data.repository.CrudRepository;

public interface ProjectCrudRepository extends CrudRepository<Project, Long > {
}
