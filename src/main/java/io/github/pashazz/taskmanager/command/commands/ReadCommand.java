package io.github.pashazz.taskmanager.command.commands;

import io.github.pashazz.taskmanager.Utils;
import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.exception.CommandException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintStream;
import java.util.Scanner;

public class ReadCommand<T>  implements Command {
    protected final Class<T> type;
    protected final CrudRepository<T, Long> repo;

    public ReadCommand(Class<T> type, CrudRepository <T, Long> repo) {
        this.type = type;
        this.repo = repo;
    }

    @Override
    @Transactional
    public void execute(Scanner in, PrintStream out) throws CommandException {
        Utils.scanIdAndDoWhileExists(type, repo, in, this, entry -> {
            out.println(entry.toString());
        });
    }
}
