package com.hemmels.javaperfmon.db;

import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.jooq.DSLContext;
import org.jooq.TableField;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.daos.EndpointDao;
import org.jooq.generated.tables.pojos.Endpoint;
import org.jooq.generated.tables.records.EndpointRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Profile("prod")
@Transactional
@Repository
@Slf4j
public class JooqService implements CommandLineRunner, DBService {

	@Autowired
	private DSLContext dsl;

	@Override
	public List<Endpoint> findAllEndpoints()
	{
		return dsl.selectFrom(Tables.ENDPOINT).fetchInto(Endpoint.class);
	}

	@Override
	public Endpoint findEndpointById(int id)
	{
		return new EndpointDao(dsl.configuration()).fetchOneById(id);
	}

	@Override
	public void incrementBadPings(List<Entry<String, Integer>> badPings)
	{
		for (Entry<String, Integer> entry : badPings) {
			TableField<EndpointRecord, Integer> todayLow = Tables.ENDPOINT.TODAY_LOW_COUNT;
			dsl.update(Tables.ENDPOINT).set(todayLow, todayLow.plus(1)).where(Tables.ENDPOINT.SITE.eq(entry.getKey()));
		}
	}

	@PostConstruct
	public void init()
	{
		log.info("Inited a JooqService; is dsl set? {}", dsl != null);
	}

	@Override
	public void run(String... args) throws Exception
	{
		log.info("Ran Jooq CommandLineRunner, dsl is null? {}", dsl == null);
	}

	@Override
	public int saveEndpoint(Endpoint endpoint)
	{
		EndpointRecord record = dsl.newRecord(Tables.ENDPOINT);
		record.from(endpoint);
		record.insert();
		return record.getId();
	}
	
	@Override
	public void resetLowCounts() {
		dsl.update(Tables.ENDPOINT).set(Tables.ENDPOINT.TODAY_LOW_COUNT, 0);
	}
}
