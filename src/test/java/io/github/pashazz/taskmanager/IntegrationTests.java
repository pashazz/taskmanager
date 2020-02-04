package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.CommandFactory;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.entity.Project;
import io.github.pashazz.taskmanager.entity.Task;
import io.github.pashazz.taskmanager.repository.PersonRepository;
import io.github.pashazz.taskmanager.repository.ProjectRepository;
import io.github.pashazz.taskmanager.repository.TaskRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import static org.hamcrest.Matchers.*;
import java.io.IOException;

/**
     * Данные интеграционные тесты опираются на предположение, что все объекты имеют
     * сквозную нумерацию ID начиная с 1.
     */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class IntegrationTests {
    /*
    Нужен для сброса запросов в БД и отработки триггеров (em.flush)

     */
    @PersistenceContext
    private EntityManager em;


    @Autowired
    private PersonRepository persons;

    @Autowired
    private TaskRepository tasks;

    @Autowired
    private ProjectRepository projects;

    @Autowired
    private CommandFactory factory;

    /**
     *  Tests creation of one person, one project and one task
     * @throws IOException
     */
    @Test
    public void testCreate() throws IOException {
        fillTestData("create1.txt");

        assertEquals(1, persons.findAll().size());
        assertEquals(1, projects.findAll().size());
        assertEquals(1, tasks.findAll().size());


        assertTrue(persons.findById(1L).isPresent());
        assertTrue(projects.findById(2L).isPresent());
        assertTrue(tasks.findById(3L).isPresent());

        var task = tasks.findById(3L).get();
        assertEquals("my test task", task.getDescription());
        assertEquals("test1", task.getName());

        var project = projects.findById(2L).get();
        assertEquals("mainproject",  project.getName());

        var person = persons.findById(1L).get();
        assertEquals("maria", person.getName());

    }

    /**
     * used by everyone except testCreate()
     */
    private void fillTestData(final String file) throws IOException {
        CommandBuilder builder = new CommandBuilder(
                new ClassPathResource(file).getInputStream(),
                System.out,
                factory
        );
        builder.execute();
        em.flush(); // This thing throws runtime exceptions on constraint violations as they're only checked by the DB
    }
    /**
     * Tests creation of person, project, task, assigning project to person, assigning task to person
     * @throws IOException
     */
    @Test
    public void testPersonAddProjectAndTask() throws IOException {
        fillTestData("create2.txt");

        assertTrue(tasks.findById(3L).isPresent());
        Task task = tasks.findById(3L).get();
        assertNotNull(task.getPerson());
        assertNotNull(task.getProject());
        assertEquals(1L, task.getPerson().getId());
        assertEquals(2L, task.getProject().getId());
    }

    /**
     * creates as in testPersonAddProjectAndTask,
     * removes a person from project
     * ensures that a task left unassigned
     * @throws IOException
     */
    @Test
    public void testRemovingPersonFromProjectUnassignsTasks() throws IOException {
        fillTestData("create2.txt");
        fillTestData("update1.txt");
        em.clear();

        assertTrue(tasks.findById(3L).isPresent());
        Task task = tasks.getOne(3L);
        assertNotNull(task.getProject());
        assertNull(task.getPerson());

        assertTrue(persons.findById(1L).isPresent());
        Person person = persons.getOne(1L);

        assertEquals(0, person.getProjects().size());
        Project project = projects.getOne(2L);
        assertEquals(0, project.getPersons().size());
        assertThat(project.getTasks(), hasItem(task));
    }

    /**
     * As in testPersonAddProjectAndTask and then remove project
     * and test if the task assigned to project is removed
     * @throws IOException
     */
    @Test
    public void testRemovingProjectRemovesTasks() throws IOException {
        fillTestData("create2.txt");
        fillTestData("remove1.txt");
        em.clear();

        assertFalse(tasks.findById(3L).isPresent());
        assertTrue(persons.findById(1L).isPresent());
        Person person = persons.getOne(1L);
        assertEquals(0, person.getProjects().size());
        assertEquals(0, person.getTasks().size());


    }

    /**
     * As in testCreate then try to assign task to user without adding the user to the project.
     *
     * It should fail
     * @throws IOException
     */
    @Test
    public void testCannotAssignTaskIfNotMemberOfProject() throws IOException {
        fillTestData("create1.txt");
        assertThrows(PersistenceException.class, () -> fillTestData("update2.txt"));
    }

    /**
     * Creates a person and a project.
     * Does not add person to a project
     * Ensures that a task is not created with that project and that user.
     * @throws IOException
     */
    @Test
    public void testCannotCreateTaskIfNotMemberOfProject() throws IOException {
        assertThrows(PersistenceException.class, () -> fillTestData("create4.txt"));
    }
}
