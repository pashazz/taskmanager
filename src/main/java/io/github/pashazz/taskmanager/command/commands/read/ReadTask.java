package io.github.pashazz.taskmanager.command.commands.read;

import io.github.pashazz.taskmanager.command.commands.ReadCommand;
import io.github.pashazz.taskmanager.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;


@Service
public class ReadTask extends ReadCommand<Task> {
    public ReadTask(CrudRepository<Task, Long> repo) {
        super(Task.class, repo);
    }
}
