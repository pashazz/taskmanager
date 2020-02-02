package io.github.pashazz.taskmanager.command.commands.update;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.command.commands.CreateUpdateCommand;
import io.github.pashazz.taskmanager.command.commands.UpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.PersonCrudRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateTask extends UpdateCommand<Task> {
    UpdateTask(CrudRepository<Task, Long> repo, PersonCrudRepository persons) {
        super(repo, Task.class);
        Map<String, CommandHandler> changeHandlers = new HashMap<>();
        changeHandlers.put("project", (in, out) -> {
            out.println("Project field is read-only");
        });
        changeHandlers.put("person", (in, out) -> {
           var person = Utils.scanId(Person.class, );
        });
    }
}
