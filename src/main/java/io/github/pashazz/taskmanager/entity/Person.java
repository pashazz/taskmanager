package io.github.pashazz.taskmanager.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {
    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    @OneToMany(mappedBy="person")
    private List<Task> tasks = new ArrayList<>();

    @Column
    @ManyToMany(targetEntity = Project.class,
    cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="person_project",
            joinColumns = @JoinColumn(name="person_id"),
            inverseJoinColumns = @JoinColumn(name="project_id"))
    private List<Project> projects = new ArrayList<>();


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Project> getProjects() {
        return projects;
    }
}
