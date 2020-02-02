package io.github.pashazz.taskmanager.command;

import io.github.pashazz.taskmanager.exception.CommandException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintStream;
import java.util.Scanner;


@Transactional // Implying that all commands deal with the DB
public interface Command {
    void execute(@NonNull final Scanner in, @NonNull final PrintStream os) throws CommandException;
}
