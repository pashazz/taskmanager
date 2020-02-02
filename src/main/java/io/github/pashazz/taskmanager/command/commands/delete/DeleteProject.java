package io.github.pashazz.taskmanager.command.commands.delete;

import io.github.pashazz.taskmanager.command.commands.DeleteCommand;
import io.github.pashazz.taskmanager.entity.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteProject extends DeleteCommand<Project> {
    public DeleteProject(CrudRepository<Project, Long> repo) {
        super(Project.class, repo);
    }
}
