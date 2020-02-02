package io.github.pashazz.taskmanager.command.commands.update;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.command.commands.CreateUpdateCommand;
import io.github.pashazz.taskmanager.command.commands.UpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.PersonCrudRepository;
import io.github.pashazz.taskmanager.repository.TaskCrudRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateProject extends UpdateCommand<Project> {
    UpdateProject(CrudRepository<Project, Long> repo, PersonCrudRepository persons, TaskCrudRepository tasks) {
        super(repo, Project.class);
        Map<String, CommandHandler> addHandlers = new HashMap<>();
        addHandlers.put("persons", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Person.class, persons, in, this,
                    person -> entry.getPersons().add(person));
        });

        Map<String, CommandHandler> removeHandlers = new HashMap<>();
        removeHandlers.put("persons", (in, out) -> {
           Utils.scanIdAndDoWhileExists(Person.class, persons, in, this,
                   person -> entry.getPersons().remove(person));
        });

        removeHandlers.put("tasks", (in, out) -> {
           Utils.scanIdAndDoWhileExists(Task.class, tasks, in, this,
                   task -> entry.getTasks().remove(task));
        });

        handlers.put("add", Utils.createMultiHandler(addHandlers, this));
        handlers.put("remove", Utils.createMultiHandler(removeHandlers, this));
    }
}
