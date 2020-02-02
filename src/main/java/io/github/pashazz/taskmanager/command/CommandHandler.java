package io.github.pashazz.taskmanager.command;

import io.github.pashazz.taskmanager.exception.CommandException;
import org.springframework.lang.NonNull;

import java.io.PrintStream;
import java.util.Scanner;

public interface CommandHandler {
    void handle(@NonNull final Scanner scanner, @NonNull final PrintStream stream) throws CommandException;
}
