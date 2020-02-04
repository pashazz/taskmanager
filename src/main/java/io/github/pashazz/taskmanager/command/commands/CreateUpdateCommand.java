package io.github.pashazz.taskmanager.command.commands;

import io.github.pashazz.taskmanager.Printable;
import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.command.CommandHandler;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;


public abstract class CreateUpdateCommand<T extends Printable> implements Command {
    private static final Log LOG  = LogFactory.getLog(CreateUpdateCommand.class);
    protected final CrudRepository<T, Long> repo;
    protected T entry;

    //Can't use T because of type erasure, so we need a T.class explicitly here;
    protected final Class<T> c;


    protected Map<String, CommandHandler> handlers = new HashMap<>();
    public CreateUpdateCommand(CrudRepository<T, Long> repo, Class<T> c)  {
        this.repo = repo;
        this.c = c;
        handlers.put(null, (in, out) -> {
            //Put default handler: sets entity fields with reflection. Suitable for strings
            String fieldName  = Utils.getWordOrQuotedText(in);
            if (fieldName == null) {
                LOG.debug("No more input");
                return;
            }

            if (fieldName.equals("id")) {
                throw new CommandException(this, "id is a read only field");
            }

            Field currentField;
            try {
                currentField = c.getDeclaredField(fieldName);
                currentField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                LOG.error("No such field: " + fieldName);
                throw new CommandException(this, "No field " + fieldName);
            }
            try {
                String value = Utils.getWordOrQuotedText(in);
                LOG.debug("Setting field " + fieldName + " with value " + value);
                currentField.set(entry, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                LOG.error("Unable to set field: " + fieldName);
                throw new CommandException(this, "Unable to set field " + fieldName);
            }
        });
    }



    @Override
    public void execute(Scanner in, PrintStream out) throws CommandException {
        var iter = handlers.keySet().iterator();
        var pattern = Utils.makePatternForHandlerMap(iter);
        while (in.hasNext()) {
            if (in.hasNext(pattern)) {
                String next = in.next();
                LOG.debug("Handling action: " + next);
                handlers.get(next).handle(in, out);
            } else {
                LOG.debug("Running default handler");
                handlers.get(null).handle(in, out);
            }
        }
        var newEntry =  repo.save(entry);
        out.println(newEntry);

    }



}
