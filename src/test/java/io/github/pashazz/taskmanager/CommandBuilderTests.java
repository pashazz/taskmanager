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

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.hamcrest.Matchers.*;
import java.io.IOException;

/**
 * Сбрасываем БД после каждого тестового метода, чтобы ID оставался стабильным
     */
@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:/schema.sql")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class CommandBuilderTests {
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

    @Autowired
    TransactionTemplate template;

    /**
     * Эти тесты не транзакционны, чтобы эмулировать работу транзакций в реальном предложении. Однако, удобно использовать
     * объекты Entity для проверки результатов, а для их загрузки нужна открытая сессия
     */
    void doInTransaction(Runnable op) {
        template.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                op.run();
            }
        });
    }

    /**
     *  Tests creation of one person, one project and one task
     * @throws IOException
     */
    @Test
    public void testCreate() throws IOException {
        fillTestData("create1.txt");

        doInTransaction(() -> {
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
            assertEquals("mainproject", project.getName());

            var person = persons.findById(1L).get();
            assertEquals("maria", person.getName());
        });
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
    }
    /**
     * Tests creation of person, project, task, assigning project to person, assigning task to person
     * @throws IOException
     */
    @Test
    public void testPersonAddProjectAndTask() throws IOException {
        fillTestData("create2.txt");
        assertTrue(tasks.findById(3L).isPresent());
        doInTransaction(() -> {
            Task task = tasks.getOne(3L);
            assertNotNull(task.getPerson());
            assertNotNull(task.getProject());
            assertEquals(1L, task.getPerson().getId());
            assertEquals(2L, task.getProject().getId());
        });
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
        fillTestData("removePersonFromProject.txt");

        assertTrue(tasks.findById(3L).isPresent());

        // We need a working session to make tests with newly created entities.


        doInTransaction(() -> {
            Task task = tasks.getOne(3L);
            assertNotNull(task.getProject());
            assertNull(task.getPerson());

            assertTrue(persons.findById(1L).isPresent());
            Person person = persons.getOne(1L);

            assertEquals(0, person.getProjects().size());
            Project project = projects.getOne(2L);
            assertEquals(0, project.getPersons().size());
            assertThat(project.getTasks(), hasItem(task));
        });

    }

    /**
     * As in testPersonAddProjectAndTask and then remove project
     * and test if the task assigned to project is removed
     * @throws IOException
     */
    @Test
    public void testRemovingProjectRemovesTasks() throws IOException {
        fillTestData("create2.txt");
        fillTestData("removeproject.txt");

        doInTransaction(() -> {
            assertFalse(projects.findById(2L).isPresent());
            assertFalse(tasks.findById(3L).isPresent());
            assertTrue(persons.findById(1L).isPresent());
            Person person = persons.getOne(1L);
            assertEquals(0, person.getProjects().size());
            assertEquals(0, person.getTasks().size());
        });



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
        fillTestData("assignTaskToPerson.txt");
        doInTransaction(() -> {
            assertTrue(persons.findById(1L).isPresent());
            assertTrue(tasks.findById(3L).isPresent());
            var task = tasks.getOne(3L);
            assertNull(task.getPerson());
            var person = persons.getOne(1L);
            assertEquals(0, person.getTasks().size());
        });
    }

    /**
     * Creates a person and a project.
     * Does not add person to a project
     * Ensures that a task is not created with that project and that user.
     * @throws IOException
     */
    @Test
    public void testCannotCreateTaskIfNotMemberOfProject() throws IOException {
        fillTestData("create4.txt");
        assertEquals(1, persons.findAll().size());
        assertEquals(1, projects.findAll().size());
        assertEquals(0, tasks.findAll().size());

    }
}
