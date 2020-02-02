package io.github.pashazz.taskmanager.command.commands.read;

import io.github.pashazz.taskmanager.command.commands.ReadCommand;
import io.github.pashazz.taskmanager.entity.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ReadPerson extends ReadCommand<Person> {
    public ReadPerson(CrudRepository<Person, Long> repo) {
        super(Person.class, repo);
    }
}

