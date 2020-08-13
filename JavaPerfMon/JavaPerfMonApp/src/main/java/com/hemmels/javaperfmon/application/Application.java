package com.hemmels.javaperfmon.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@ComponentScan(basePackages = {"com.hemmels.javaperfmon.application", "com.hemmels.javaperfmon.bean", "com.hemmels.javaperfmon.db"})
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
@EnableTransactionManagement
@SpringBootApplication
@Slf4j
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
		log.debug("main returned in Application, services are running");
	}
}
