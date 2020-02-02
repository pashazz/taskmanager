package io.github.pashazz.taskmanager.command.commands.delete;

import io.github.pashazz.taskmanager.command.commands.DeleteCommand;
import io.github.pashazz.taskmanager.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class DeletePerson extends DeleteCommand<Person> {

    public DeletePerson(CrudRepository<Person, Long> repo) {
        super(Person.class,repo);

    }
}
