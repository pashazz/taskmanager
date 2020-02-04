package io.github.pashazz.taskmanager.entity;

import io.github.pashazz.taskmanager.Printable;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Task implements Printable {
    @Column (nullable = false, updatable = false)
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name="project_fk", nullable = false)
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Task{");
        sb.append("id=").append(id);
        sb.append(", project=").append(project == null ? "not assigned" :project.shortToString());
        sb.append(", person=").append(person == null ? "not assigned" : person.shortToString());
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String shortToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(" - '");
        sb.append(name);
        sb.append("'");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Task) {
            Task task = (Task) o;
            return id.equals(task.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
