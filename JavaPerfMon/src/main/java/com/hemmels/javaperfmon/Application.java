package com.hemmels.javaperfmon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement
@SpringBootApplication
@Slf4j
public class Application {

	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
		GreetingClient gwc = new GreetingClient();
		log.info(gwc.hello());
	}
}
