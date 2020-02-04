package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.CommandFactory;
import io.github.pashazz.taskmanager.command.CommandFactoryImpl;
import io.github.pashazz.taskmanager.exception.CommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedCommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedEntityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CommandBuilder {
    public CommandBuilder(InputStream stream, PrintStream outputStream, CommandFactory factory) {
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
        while (scanner.hasNextLine()) {
            if (scanner.hasNext(Pattern.compile("q|quit"))) {
                System.exit(0);
            } else {
                String line = scanner.nextLine();
                LOG.debug("Read line: " + line);
                try {
                    var lineScanner = new Scanner(new ByteArrayInputStream(line.getBytes()));
                    var command = factory.getCommandFromInput(lineScanner);
                    if (command != null) {
                        LOG.debug("Executing command: " + command);
                        command.execute(lineScanner, os);
                        continue;
                    }
                    else {
                        LOG.debug("Skipping empty line");
                        continue;
                    }
                } catch (UnrecognizedEntityException e) {
                    os.printf("Unrecognized entity: %s\n", e.getMessage());
                } catch (UnrecognizedCommandException e) {
                    os.printf("Unrecognized command: %s\n", e.getMessage());
                } catch (CommandException e) {
                    os.printf("%s\n", e.getMessage());
                } catch (DataIntegrityViolationException e) {
                    os.printf("Incorrect data format: %s\n", e.getMessage());
                } catch (DataAccessException e) {
                    os.printf("Data access exception: %s\n", e.getMessage());
                }
            }
        }
    }
}
