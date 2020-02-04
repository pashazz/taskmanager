package io.github.pashazz.taskmanager.command.commands.update;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.command.commands.UpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.PersonRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateTask extends UpdateCommand<Task> {
    UpdateTask(CrudRepository<Task, Long> repo, PersonRepository persons) {
        super(repo, Task.class);
        Map<String, CommandHandler> changeHandlers = new HashMap<>();
        changeHandlers.put("project", (in, out) -> {
            out.println("Project field is read-only");
        });
        changeHandlers.put("person", (in, out) -> {
           var person = Utils.scanId(Person.class, persons, in, this);
           entry.getPerson().getTasks().remove(entry);
           entry.setPerson(person);
        });

        Map<String, CommandHandler> removeHandlers = new HashMap<>();
        removeHandlers.put("project", (in, out) -> {
            out.println("Project field is read-only");
        });
        removeHandlers.put("person", (in, out) -> {
            entry.getPerson().getTasks().remove(entry);
            entry.setPerson(null);
        });

        handlers.put("set", Utils.createMultiHandler(changeHandlers, this));
        handlers.put("unset", Utils.createMultiHandler(removeHandlers, this));
        handlers.put("project", (in, out) -> {
           //out.println("use 'update task set project <id>' or update task unset project");
        });
        handlers.put("person", (in, out) -> {
            out.println("use 'update task set person <id>' or update task unset person");
        });
    }
}
