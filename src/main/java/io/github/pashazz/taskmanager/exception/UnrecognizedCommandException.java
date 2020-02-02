package io.github.pashazz.taskmanager.exception;

public class UnrecognizedCommandException extends Throwable {
    private final String command;

    String getCommand() {
        return command;
    }
    public UnrecognizedCommandException(String command) {
        super("Unrecognized command: " + command + "; available commands: create, read, update, delete");
        this.command = command;
    }

}
