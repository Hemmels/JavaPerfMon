package com.hemmels.javaperfmon.bean;

import java.util.List;

import javax.annotation.PostConstruct;

import org.jooq.generated.tables.pojos.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DatabaseService {

	@Autowired
	private JdbcTemplate jdbc;

	public List<Endpoint> findAllEndpoints()
	{
		String sql = "SELECT * FROM endpoint";
		return jdbc.query(sql, new BeanPropertyRowMapper<>(Endpoint.class));
	}

	public Endpoint findEndpointById(Long id)
	{
		String sql = "SELECT * FROM endpoint WHERE id = ?";
		return jdbc.queryForObject(sql, new BeanPropertyRowMapper<>(Endpoint.class));
	}

	@PostConstruct
	public void init()
	{
		log.info("Inited a DatabaseService; is jdbc set? {}", jdbc != null);
	}
}
