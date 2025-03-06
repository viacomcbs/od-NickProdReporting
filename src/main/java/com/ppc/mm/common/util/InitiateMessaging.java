package com.ppc.mm.common.util;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ppc.mm.common.config.ApplicationConfiguration;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class InitiateMessaging implements CommandLineRunner {
	
	@Autowired
	DataSource dataSource;

	@Autowired
	private ApplicationContext appContext;

    private static final Logger logger = LoggerFactory.getLogger(InitiateMessaging.class);
	
	public static void main(String[] args) {
		String environment = System.getenv("mmapp");
		logger.info("environment==={}",environment);
		if("Q03".equals(environment)) {
			System.setProperty("TEAMS_HOME", "/opt/opentext/otmm");
		} else {
			System.setProperty("TEAMS_HOME", "/opt/opentext/otmm");
		}

		SpringApplication.run(ApplicationConfiguration.class, args);
		SpringApplication.run(InitiateMessaging.class, args);

	}

	@Override
	public void run(String... args) {
		logger.info("DATASOURCE = {}" , dataSource.toString());
	/*	String[] beans = appContext.getBeanDefinitionNames();
		Arrays.sort(beans);
		for (String beanName : beans) {
			logger.info(beanName);
		}*/
	}
}