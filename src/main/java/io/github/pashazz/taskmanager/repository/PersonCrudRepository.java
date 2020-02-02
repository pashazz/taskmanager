package io.github.pashazz.taskmanager.repository;

import io.github.pashazz.taskmanager.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonCrudRepository extends CrudRepository<Person, Long> {

}
