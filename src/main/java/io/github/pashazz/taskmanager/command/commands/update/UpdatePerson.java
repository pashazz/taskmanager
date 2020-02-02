package io.github.pashazz.taskmanager.command.commands.update;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.command.commands.CreateUpdateCommand;
import io.github.pashazz.taskmanager.command.commands.UpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.ProjectCrudRepository;
import io.github.pashazz.taskmanager.repository.TaskCrudRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdatePerson extends UpdateCommand<Person> {
    UpdatePerson(CrudRepository<Person, Long> repo, ProjectCrudRepository projects, TaskCrudRepository tasks) {
        super(repo, Person.class);
        Map<String, CommandHandler> addHandlers = new HashMap<>();
        addHandlers.put("projects", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Project.class, projects, in, this, project -> {
                entry.getProjects().add(project);
            });
        });
        addHandlers.put("tasks", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Task.class, tasks, in, this, task -> {
                entry.getTasks().add(task);
            });
        });

        Map<String, CommandHandler> removeHandlers = new HashMap<>();
        removeHandlers.put("projects", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Project.class, projects, in, this,
                    project -> {
                        entry.getProjects().remove(project);
                    });
        });
        removeHandlers.put("tasks", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Task.class, tasks, in, this,
                    task -> {
                        entry.getTasks().add(task);
                    });
        });

        handlers.put("add", Utils.createMultiHandler(addHandlers, this));
        handlers.put("remove", Utils.createMultiHandler(removeHandlers, this));
    }
}
