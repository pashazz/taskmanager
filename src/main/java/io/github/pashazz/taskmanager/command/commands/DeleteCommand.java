package io.github.pashazz.taskmanager.command.commands;

import io.github.pashazz.taskmanager.Printable;
import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.PrintStream;
import java.util.Scanner;



public class DeleteCommand<T extends Printable> implements Command {
    protected final Class<T> type;
    protected final CrudRepository<T, Long> repo;


    public DeleteCommand(Class<T> type, CrudRepository<T, Long> repo) {
        this.type = type;
        this.repo = repo;
    }

    private final Log LOG = LogFactory.getLog(DeleteCommand.class);
    @Override
    public void execute(final Scanner in, final PrintStream out) throws CommandException {
        Utils.scanIdAndDoWithEntityWhileExists(type, repo, in,this,  entry -> {
            LOG.debug("Deleting object of type " + type.getName() + ": " + entry );
            repo.delete(entry);
        });
    }
}
