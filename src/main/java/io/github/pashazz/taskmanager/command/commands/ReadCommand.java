package io.github.pashazz.taskmanager.command.commands;

import io.github.pashazz.taskmanager.Printable;
import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintStream;
import java.util.Scanner;

public class ReadCommand<T extends Printable>  implements Command {
    protected final Class<T> type;
    protected final CrudRepository<T, Long> repo;

    public ReadCommand(Class<T> type, CrudRepository <T, Long> repo) {
        this.type = type;
        this.repo = repo;
    }

    @Override
    @Transactional
    public void execute(Scanner in, PrintStream out) throws CommandException {
        if (in.hasNext("all")) {
            repo.findAll().forEach(item -> out.println(item.printOut()));
            return;
        }
        Utils.scanIdAndDoWithEntityWhileExists(type, repo, in, this, entry -> {
            out.println(entry.printOut());
        });
    }
}
