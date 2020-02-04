package io.github.pashazz.taskmanager.command.commands.update;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.command.commands.UpdateCommand;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.ProjectRepository;
import io.github.pashazz.taskmanager.repository.TaskRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.Map;

@Service
public class UpdatePerson extends UpdateCommand<Person> {
    private static Log LOG = LogFactory.getLog(UpdatePerson.class);

    /**
     * Default order
     * inserts
     * updates
     * deletions of collections elements
     * inserts of the collection elements
     * deletes
     * Here we have many to many projects <-> persons relation. Task depends on this relation.
     * But updates are done before inserts of collection elements.
     * Therefore we need to flush every time after insert of collection / delete of collection happens.
     * There are only one
     */
    @PersistenceContext
    private EntityManager em;

    UpdatePerson(CrudRepository<Person, Long> repo, ProjectRepository projects, TaskRepository tasks) {
        super(repo, Person.class);
        Map<String, CommandHandler> addHandlers = new HashMap<>();

        addHandlers.put("projects", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Project.class, projects, in, this, project -> {
                entry.getProjects().add(project);
                project.getPersons().add(entry);
                LOG.debug("to person " + entry +" added project " + project);
                em.flush(); //We indicate specifically that we need to override default hibernate order
            });
        });
        addHandlers.put("tasks", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Task.class, tasks, in, this, task -> {
                entry.getTasks().add(task);
                task.setPerson(entry);
                LOG.debug("to person " + entry +" added task " + task);
            });
        });

        Map<String, CommandHandler> removeHandlers = new HashMap<>();
        removeHandlers.put("projects", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Project.class, projects, in, this,
                    project -> {
                        entry.getProjects().remove(project);
                        project.getPersons().remove(entry);
                        em.flush();
                    });
        });
        removeHandlers.put("tasks", (in, out) -> {
            Utils.scanIdAndDoWhileExists(Task.class, tasks, in, this,
                    task -> {
                        entry.getTasks().remove(task);
                        task.setPerson(null);
                    });
        });

        handlers.put("add", Utils.createMultiHandler(addHandlers, this));
        handlers.put("remove", Utils.createMultiHandler(removeHandlers, this));
    }
}
