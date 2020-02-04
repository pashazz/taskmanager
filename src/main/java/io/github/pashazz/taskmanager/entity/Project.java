package io.github.pashazz.taskmanager.entity;

import io.github.pashazz.taskmanager.Printable;
import io.github.pashazz.taskmanager.Utils;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Project implements Printable {
    @Column
    @Id
    @GeneratedValue()
    private Long id;

    @Column
    @NonNull
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "project", orphanRemoval = true, cascade = CascadeType.ALL)
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Project{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", tasks=").append(Utils.iteratorToShortString(tasks.iterator()));
        sb.append(", persons=").append(Utils.iteratorToShortString(persons.iterator()));
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
        if (o instanceof Project) {
            Project project = (Project) o;
            return id.equals(project.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
