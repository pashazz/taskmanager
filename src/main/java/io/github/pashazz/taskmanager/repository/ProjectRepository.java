package io.github.pashazz.taskmanager.repository;

import io.github.pashazz.taskmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long > {
}
