package io.github.pashazz.taskmanager.command.commands.create;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.commands.CreateCommand;
import io.github.pashazz.taskmanager.command.commands.CreateUpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.ProjectCrudRepository;
import io.github.pashazz.taskmanager.repository.TaskCrudRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public class CreatePerson extends CreateCommand<Person> {
    CreatePerson(CrudRepository<Person, Long> repo,
                 ProjectCrudRepository projects, TaskCrudRepository tasks) {
        super(repo, Person.class);
        handlers.put("projects", (in, out)-> {
            Utils.scanIdAndDoWhileExists(Project.class, projects, in, this, project -> {
                entry.getProjects().add(project);
            });
        });
        handlers.put("tasks", (in, out)-> {
            Utils.scanIdAndDoWhileExists(Task.class, tasks, in, this, task -> {
                entry.getTasks().add(task);
            });
        });
    }
}
