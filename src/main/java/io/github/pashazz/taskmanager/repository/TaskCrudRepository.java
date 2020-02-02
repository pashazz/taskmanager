package io.github.pashazz.taskmanager.repository;

import io.github.pashazz.taskmanager.entity.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskCrudRepository extends CrudRepository<Task, Long> {
}
