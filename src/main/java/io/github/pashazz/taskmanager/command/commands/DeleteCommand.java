package io.github.pashazz.taskmanager.command.commands;

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



public class DeleteCommand<T> implements Command {
    protected final Class<T> type;
    protected final CrudRepository<T, Long> repo;

    @PersistenceContext
    private EntityManager em;
    public DeleteCommand(Class<T> type, CrudRepository<T, Long> repo) {
        this.type = type;
        this.repo = repo;
    }

    private final Log LOG = LogFactory.getLog(DeleteCommand.class);
    @Override
    public void execute(final Scanner in, final PrintStream out) throws CommandException {
        Utils.scanIdAndDoWithIdWhileExists(type, repo, in,this,  id -> {
            LOG.debug("Deleting entry by id: " + id);
            repo.deleteById(id);
        });
    }
}
