package io.github.pashazz.taskmanager.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.h2.tools.Server;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Arrays;

@Configuration
public class AppConfig {
    private static Log LOG = LogFactory.getLog(AppConfig.class);

    @Autowired
    private Environment environment;

    @Bean
    public DataSourceInitializer dataSourceInitializer(@Autowired final DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(new ClassPathResource("/schema.sql"));
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        if (!Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            LOG.debug("Filling DB for production");
            resourceDatabasePopulator.addScript(new ClassPathResource("/data.sql"));
        } else {
            LOG.debug("Preparing DB for test");
        }
        return dataSourceInitializer;
    }

}
