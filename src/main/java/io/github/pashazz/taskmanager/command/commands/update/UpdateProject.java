package io.github.pashazz.taskmanager.command.commands.update;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.command.commands.UpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.PersonRepository;
import io.github.pashazz.taskmanager.repository.TaskRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdateProject extends UpdateCommand<Project> {

    /**
     * See note in UpdatePerson.java
     */
    @PersistenceContext
    private EntityManager em;

    UpdateProject(CrudRepository<Project, Long> repo, PersonRepository persons, TaskRepository tasks) {
        super(repo, Project.class);
        Map<String, CommandHandler> addHandlers = new HashMap<>();
        addHandlers.put("persons", (in, out) -> {
            Utils.scanIdAndDoWithEntityWhileExists(Person.class, persons, in, this,
                    person -> {
                        entry.getPersons().add(person);
                        person.getProjects().add(entry);
                        em.flush();
                    });
        });

        Map<String, CommandHandler> removeHandlers = new HashMap<>();
        removeHandlers.put("persons", (in, out) -> {
           Utils.scanIdAndDoWithEntityWhileExists(Person.class, persons, in, this,
                   person -> {
                       entry.getPersons().remove(person);
                       person.getProjects().remove(entry);
                       em.flush();
                   });
        });

        removeHandlers.put("tasks", (in, out) -> {
           Utils.scanIdAndDoWithEntityWhileExists(Task.class, tasks, in, this,
                   task -> {
                       entry.getTasks().remove(task);
                       task.setPerson(null);
                   });
        });

        handlers.put("add", Utils.createMultiHandler(addHandlers, this));
        handlers.put("remove", Utils.createMultiHandler(removeHandlers, this));
    }
}
