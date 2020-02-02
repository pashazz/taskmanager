package io.github.pashazz.taskmanager.command.commands.create;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.commands.CreateCommand;
import io.github.pashazz.taskmanager.command.commands.CreateUpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.PersonCrudRepository;
import io.github.pashazz.taskmanager.repository.ProjectCrudRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class CreateTask extends CreateCommand<Task> {
    CreateTask(CrudRepository<Task, Long> repo, ProjectCrudRepository projects, PersonCrudRepository persons) {
        super(repo, Task.class);
        handlers.put("project", (in, out) -> {
            var project = Utils.scanId(Project.class, projects, in, this);
            entry.setProject(project);
        });

        handlers.put("person", (in, out) -> {
            var person = Utils.scanId(Person.class, persons, in, this);
            entry.setPerson(person);
        });
    }
}
