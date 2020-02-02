package io.github.pashazz.taskmanager.exception;

import io.github.pashazz.taskmanager.command.Command;

public class CommandException extends Throwable {
    private final Command command;
    private final String details;

    public CommandException(Command command, String details) {
        super("Command exception: " + command.toString() + ": " + details);
        this.command = command;
        this.details = details;
    }

    public Command getCommand() {
        return command;
    }
}
