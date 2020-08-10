package com.hemmels.javaperfmon.application;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jooq.generated.tables.pojos.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hemmels.javaperfmon.bean.DatabaseService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Service
@Slf4j
public class GreetingController {

	@Autowired
	private DatabaseService ds;

	@PostConstruct
	public void init()
	{
		log.info("Inited a DatabaseService; is ds set? {}", ds != null);
	}

	@RequestMapping("/")
	public String hello()
	{
		return "Hello World! This is me!";
	}

	@RequestMapping("/endpoints")
	public String endpoints()
	{
		return ds.findAllEndpoints().stream().map(Endpoint::getSite).collect(Collectors.joining("<br />"));
	}
}
