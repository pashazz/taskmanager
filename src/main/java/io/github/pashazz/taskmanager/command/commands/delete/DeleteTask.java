package io.github.pashazz.taskmanager.command.commands.delete;

import io.github.pashazz.taskmanager.command.commands.DeleteCommand;
import io.github.pashazz.taskmanager.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

@Service
public class DeleteTask extends DeleteCommand<Task> {
    public DeleteTask(CrudRepository<Task, Long> repo) {
        super(Task.class, repo);
    }
}
