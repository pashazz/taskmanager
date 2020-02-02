package io.github.pashazz.taskmanager.exception;

public class UnrecognizedEntityException extends Throwable {
    private final String entity;

    public UnrecognizedEntityException(String entity) {
        super("Unrecognized entity: " + entity  +"; available entities: person, project, task");
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }
}

