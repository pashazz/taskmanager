package io.github.pashazz.taskmanager.entity;

import io.github.pashazz.taskmanager.ShortStringRepresentable;
import io.github.pashazz.taskmanager.Utils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Person implements ShortStringRepresentable {
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

    @Override
    public String toString() {

        final StringBuilder sb = new StringBuilder("Person{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", tasks=").append(Utils.iteratorToShortString(tasks.iterator()));
        sb.append(", projects=").append(Utils.iteratorToShortString(projects.iterator()));
        sb.append("}");


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
        if (o instanceof Person) { // I am aware that the proxies will evaluate this to true.
            Person person = (Person) o;
            return id.equals(person.getId());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
