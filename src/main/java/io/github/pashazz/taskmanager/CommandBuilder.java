package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.CommandFactory;
import io.github.pashazz.taskmanager.command.CommandFactoryImpl;
import io.github.pashazz.taskmanager.exception.CommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedCommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedEntityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CommandBuilder {
    CommandBuilder(InputStream stream, PrintStream outputStream, CommandFactory factory) {
        this.is = stream;
        this.os = outputStream;
        this.factory = factory;
    }

    private final CommandFactory factory;
    private final InputStream is;
    private final PrintStream os;

    private final Log LOG = LogFactory.getLog(CommandBuilder.class);

    public void execute() {
        Scanner scanner = new Scanner(is);
        while (true) {
            if (scanner.hasNext(Pattern.compile("q|quit"))) {
                System.exit(0);
            } else {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    try {
                        var command = factory.getCommandFromInput(new Scanner(new ByteArrayInputStream(line.getBytes())));
                        if (command != null)
                            command.execute(os);
                        else {
                            LOG.debug("Skipping empty line");
                            continue;
                        }
                    } catch (UnrecognizedEntityException e) {
                        os.printf("Unrecognized entity: %s\n", e.getMessage());
                    } catch (UnrecognizedCommandException e) {
                        os.printf("Unrecognized command: %s\n", e.getMessage());
                    } catch (CommandException e) {
                        os.printf("Command error: %s\n", e.getMessage());
                    }
                }
            }
        }
    }
}
