package io.github.pashazz.taskmanager.entity;

import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
public class Task {
    @Column (nullable = false, updatable = false)
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @Column(nullable = false, updatable = false)
    @JoinColumn(name="project_fk")
    private Project project;

    @ManyToOne
    @JoinColumn(name="person_fk")
    private Person person;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    public Long getId() {
        return id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

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
}
