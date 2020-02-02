package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.Command;
import io.github.pashazz.taskmanager.command.CommandFactory;
import io.github.pashazz.taskmanager.command.CommandFactoryImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;
import java.util.regex.Pattern;

@SpringBootApplication
public class TaskmanagerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}


	private static Log LOG = LogFactory.getLog(TaskmanagerApplication.class);

	@Autowired
	private CommandFactory factory;

	@Override
	public void run(String... args) throws Exception {
		LOG.debug("The application has started");
		CommandBuilder cb = new CommandBuilder(System.in, System.out, factory);
		cb.execute();
	}
}
