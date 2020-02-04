package io.github.pashazz.taskmanager.command.commands.create;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.commands.CreateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.repository.PersonRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public class CreateProject extends CreateCommand<Project> {
    CreateProject(CrudRepository<Project, Long> repo, PersonRepository persons) {
        super(repo, Project.class);
        handlers.put("persons", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Person.class, persons, in, this,
                    person -> {
                        entry.getPersons().add(person);
                        person.getProjects().add(entry);
                    });
        });

    }
}
