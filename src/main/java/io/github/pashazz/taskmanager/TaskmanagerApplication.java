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
public class TaskmanagerApplication  {
	public static void main(String[] args) {
		SpringApplication.run(TaskmanagerApplication.class, args);
	}
}
