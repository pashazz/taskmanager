package io.github.pashazz.taskmanager.command.commands.read;

import io.github.pashazz.taskmanager.command.commands.ReadCommand;
import io.github.pashazz.taskmanager.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class ReadProject extends ReadCommand<Project> {
    public ReadProject(CrudRepository<Project, Long> repo) {
        super(Project.class, repo);
    }
}
