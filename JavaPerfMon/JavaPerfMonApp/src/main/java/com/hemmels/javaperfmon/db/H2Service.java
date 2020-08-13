package com.hemmels.javaperfmon.db;

import java.util.List;

import javax.annotation.PostConstruct;

import org.jooq.generated.tables.pojos.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Profile("test")
@Repository
@Slf4j
public class H2Service implements DBService {

	@Autowired
	private JdbcTemplate jdbc;

	public List<Endpoint> findAllEndpoints()
	{
		String sql = "SELECT * FROM endpoint";
		return jdbc.query(sql, new BeanPropertyRowMapper<>(Endpoint.class));
	}

	public Endpoint findEndpointById(int id)
	{
		String sql = "SELECT * FROM endpoint WHERE id = ?";
		return jdbc.queryForObject(sql, new Object[]{id}, Endpoint.class);
	}

	public Endpoint findEndpointByName(String name)
	{
		String sql = "SELECT * FROM endpoint WHERE name = ?";
		return jdbc.queryForObject(sql, new Object[]{name}, Endpoint.class);
	}

	@PostConstruct
	public void init()
	{
		log.info("Inited an embedded DatabaseService; is jdbc set? {}", jdbc != null);
	}

	@Override
	public int saveEndpoint(Endpoint endpoint)
	{
		jdbc.update("INSERT INTO endpoint (site) VALUES (?)", endpoint.getSite());
		// TODO: Return new id
		return 1;
	}
}
