package io.github.pashazz.taskmanager.command;

import io.github.pashazz.taskmanager.command.commands.CreateCommand;
import io.github.pashazz.taskmanager.command.commands.DeleteCommand;
import io.github.pashazz.taskmanager.command.commands.ReadCommand;
import io.github.pashazz.taskmanager.command.commands.create.CreatePerson;
import io.github.pashazz.taskmanager.command.commands.create.CreateProject;
import io.github.pashazz.taskmanager.command.commands.create.CreateTask;
import io.github.pashazz.taskmanager.command.commands.delete.DeletePerson;
import io.github.pashazz.taskmanager.command.commands.delete.DeleteProject;
import io.github.pashazz.taskmanager.command.commands.delete.DeleteTask;
import io.github.pashazz.taskmanager.command.commands.read.ReadPerson;
import io.github.pashazz.taskmanager.command.commands.read.ReadProject;
import io.github.pashazz.taskmanager.command.commands.read.ReadTask;
import io.github.pashazz.taskmanager.command.commands.update.UpdatePerson;
import io.github.pashazz.taskmanager.command.commands.update.UpdateProject;
import io.github.pashazz.taskmanager.command.commands.update.UpdateTask;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.exception.CommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedCommandException;
import io.github.pashazz.taskmanager.exception.UnrecognizedEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class CommandFactoryImpl implements CommandFactory {

    private enum Action {
        CREATE, READ, UPDATE, DELETE;
    }

    private enum Type {
        PERSON, PROJECT, TASK
    }

    @Autowired
    private CreatePerson createPerson;

    @Autowired
    private ReadPerson readPerson;

    @Autowired
    private UpdatePerson updatePerson;

    @Autowired
    private DeletePerson deletePerson;


    @Autowired
    private CreateTask createTask;

    @Autowired
    private ReadTask readTask;

    @Autowired
    private UpdateTask updateTask;

    @Autowired
    private DeleteTask deleteTask;

    @Autowired
    private CreateProject createProject;

    @Autowired
    private ReadProject readProject;

    @Autowired
    private UpdateProject updateProject;

    @Autowired
    private DeleteProject deleteProject;


    @Override
    public Command getCommandFromInput(Scanner input) throws UnrecognizedCommandException, UnrecognizedEntityException {
        if (!input.hasNext()) {
            return null;
        }
        if (!input.hasNext("create|read|update|delete")) {
            if (input.hasNext())
                throw new UnrecognizedCommandException(input.next());
            else {
                throw new UnrecognizedCommandException("<empty>");
            }
        }
        Action action = Action.valueOf(input.next().toUpperCase());
        if (!input.hasNext("person|project|task")) {
            if (input.hasNext())
                throw new UnrecognizedEntityException(input.next());
            else {
                throw new  UnrecognizedEntityException("<empty>");
            }
        }
        Type type = Type.valueOf(input.next().toUpperCase());
        // I might use a static  map as well though.
        switch (action) {
            case CREATE:
                switch (type) {
                    case PERSON:
                        return createPerson;
                    case TASK:
                        return createTask;
                    case PROJECT:
                        return createProject;
                }
            case READ:
                switch (type) {
                    case PERSON:
                        return readPerson;
                    case TASK:
                        return readTask;
                    case PROJECT:
                        return readProject;
                }
            case UPDATE:
                switch (type) {
                    case PERSON:
                        return updatePerson;
                    case TASK:
                        return updateTask;
                    case PROJECT:
                        return updateProject;
                }
            case DELETE:
                switch (type){
                    case PERSON:
                        return deletePerson;
                    case TASK:
                        return deleteTask;
                    case PROJECT:
                        return deleteProject;
                }
            default:
                throw new RuntimeException();
        }
    }
}
