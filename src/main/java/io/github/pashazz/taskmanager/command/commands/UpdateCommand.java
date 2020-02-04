package io.github.pashazz.taskmanager.command.commands;

import io.github.pashazz.taskmanager.Printable;
import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.repository.CrudRepository;

import java.io.PrintStream;
import java.util.Scanner;

public class UpdateCommand<T extends Printable> extends CreateUpdateCommand<T> {

    public UpdateCommand(CrudRepository<T, Long> repo, Class<T> c) {
        super(repo, c);
    }

    private static Log LOG = LogFactory.getLog(UpdateCommand.class);
    @Override
    public void execute(Scanner in, PrintStream out) throws CommandException {
        entry = Utils.scanIdReturnEntity(c, repo, in, this);
        LOG.debug("Updating entry " + entry);
        super.execute(in, out);
    }
}
