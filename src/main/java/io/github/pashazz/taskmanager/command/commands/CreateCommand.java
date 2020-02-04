package io.github.pashazz.taskmanager.command.commands;

import io.github.pashazz.taskmanager.Printable;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.repository.CrudRepository;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;


public class CreateCommand<T extends Printable> extends CreateUpdateCommand<T> {
    public CreateCommand(CrudRepository<T, Long> repo, Class<T> c) {
        super(repo, c);
    }

    private static Log LOG = LogFactory.getLog(CreateCommand.class);
    @Override
    public void execute(Scanner in, PrintStream out) throws CommandException {
        LOG.debug("Creating a new entry");
        try {
            entry = c.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOG.error("No  no-argument constructor, failing with " + e.getMessage());
                throw new CommandException(this, e.getMessage());
        }
        super.execute(in, out);
    }
}
