package com.hemmels.javaperfmon.db;

import java.util.List;
import java.util.Map.Entry;

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

	@Override
	public List<Endpoint> findAllEndpoints()
	{
		String sql = "SELECT * FROM endpoint";
		return jdbc.query(sql, new BeanPropertyRowMapper<>(Endpoint.class));
	}

	@Override
	public Endpoint findEndpointById(int id)
	{
		String sql = "SELECT * FROM endpoint WHERE id = ?";
		return jdbc.queryForObject(sql, new Object[]{id}, Endpoint.class);
	}

	@Override
	public void incrementBadPings(List<Entry<String, Integer>> badPings)
	{
		for (Entry<String, Integer> entry : badPings) {
			jdbc.update("UPDATE endpoint SET today_low_count = today_low_count + 1 WHERE site = ?", entry.getKey());
		}
	}
	
	@Override
	public void resetLowCounts() {
		jdbc.update("UPDATE endpoint SET today_low_count = 0");
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
