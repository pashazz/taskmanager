package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.commands.read.ReadPerson;
import io.github.pashazz.taskmanager.entity.Person;
import io.github.pashazz.taskmanager.repository.PersonRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import static org.mockito.Mockito.*;

@SpringJUnitConfig
public class ReadTest {

    @MockBean
    private PersonRepository persons;

    private static Log LOG = LogFactory.getLog(ReadTest.class);

    static Scanner getTestScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    static List<Person> getTestPersonData() {
        List<Person> people = new ArrayList<>();

        Person johnLennon = new Person();
        johnLennon.setId(1L);
        johnLennon.setName("John Lennon");
        johnLennon.setEmail("mail@mail");

        Person billGates = new Person();
        billGates.setId(2L);
        billGates.setName("Bill Gates");
        billGates.setEmail("gates@microsoft.com");
        people.add(spy(johnLennon));
        people.add(spy(billGates));
        return people;
    }

    @Test
    void test_readPerson_onAllcallsFindAll() {
        var in = getTestScanner("all");
        var out = new ByteArrayOutputStream();
        var data = getTestPersonData();

        when(persons.findAll()).thenReturn(data);
        ReadPerson rp = new ReadPerson(persons);
        assertDoesNotThrow(() -> rp.execute(in, new PrintStream(out)));

        LOG.info("Checking that findAll was called once");
        verify(persons, times(1)).findAll();

        LOG.debug("Checking that printOut was called for each item");
        data.forEach(person -> verify(person, times(1)).printOut());
    }

    @Test
    void test_readPerson_callsFindById() {
        var in = getTestScanner("2");
        var out = new ByteArrayOutputStream();
        Person johnLennon = new Person();
        johnLennon.setId(1L);
        johnLennon.setName("John Lennon");
        johnLennon.setEmail("mail@mail");

        johnLennon = spy(johnLennon);

        Person billGates = new Person();
        billGates.setId(2L);
        billGates.setName("Bill Gates");
        billGates.setEmail("gates@microsoft.com");

        billGates = spy(billGates);

        when(persons.findById(1L)).thenReturn(Optional.of(johnLennon));
        when(persons.findById(2L)).thenReturn(Optional.of(billGates));
        ReadPerson rp = new ReadPerson(persons);
        assertDoesNotThrow(() -> rp.execute(in, new PrintStream(out)));
        verify(persons, times(1)).findById(2L);
        verify(persons, times(0)).findById(1L);
        verify(persons, times(1)).findById(any());

        verify(billGates, times(1)).printOut();
        verify(johnLennon, times(0)).printOut();
    }
}
