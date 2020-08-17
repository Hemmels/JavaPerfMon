package com.hemmels.javaperfmon.db;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.jooq.DSLContext;
import org.jooq.SelectWhereStep;
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
	public List<Endpoint> findAllEndpoints(Boolean enabled)
	{
		SelectWhereStep<EndpointRecord> selectQuery = dsl.selectFrom(Tables.ENDPOINT);
		if (enabled != null) {
			selectQuery = (SelectWhereStep<EndpointRecord>) selectQuery.where(Tables.ENDPOINT.ENABLED.eq(enabled));
		}
		return selectQuery.fetchInto(Endpoint.class);
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
			dsl.update(Tables.ENDPOINT).set(Tables.ENDPOINT.TODAY_LOW_COUNT, 0).set(Tables.ENDPOINT.ENABLED, false)
					.where(Tables.ENDPOINT.SITE.eq(entry.getKey())).execute();
			dsl.insertInto(Tables.DOWN_LOG).columns(Tables.DOWN_LOG.SITE, Tables.DOWN_LOG.DOWNSTAMP).values(entry.getKey(),
					LocalDateTime.now()).execute();
			log.debug("{} was found bad, disabling and logging", entry.getKey());
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
	public void resetLowCounts()
	{
		dsl.update(Tables.ENDPOINT).set(Tables.ENDPOINT.TODAY_LOW_COUNT, 0);
	}
}
