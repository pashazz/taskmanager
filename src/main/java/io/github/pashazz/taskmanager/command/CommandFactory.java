package io.github.pashazz.taskmanager.command;

import io.github.pashazz.taskmanager.exception.CommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedCommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedEntityException;
import org.springframework.lang.NonNull;

import java.util.Scanner;

public interface CommandFactory {
    Command getCommandFromInput(@NonNull final Scanner input) throws UnrecognizedCommandException, UnrecognizedEntityException, CommandException;
}
