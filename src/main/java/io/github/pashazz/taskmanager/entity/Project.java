package io.github.pashazz.taskmanager.entity;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Column
    @Id
    @GeneratedValue
    private Long id;

    @Column
    @NonNull
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(mappedBy = "projects")
    private List<Person> persons = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
