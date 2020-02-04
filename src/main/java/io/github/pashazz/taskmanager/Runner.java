package io.github.pashazz.taskmanager;

import io.github.pashazz.taskmanager.command.CommandFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class Runner implements CommandLineRunner {
	private static Log LOG = LogFactory.getLog(Runner.class);

	@Autowired
	protected CommandFactory factory;

	@Override
	public void run(String... args) throws Exception {
		LOG.debug("The application has started");
		CommandBuilder cb = new CommandBuilder(System.in, System.out, factory);
		cb.execute();
	}
}
