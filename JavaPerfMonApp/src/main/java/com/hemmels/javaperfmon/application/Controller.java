package com.hemmels.javaperfmon.application;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.jooq.generated.tables.pojos.Endpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hemmels.javaperfmon.bean.ServiceHandler;
import com.hemmels.javaperfmon.db.DBService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Service
@Slf4j
public class Controller {

	@Autowired
	private DBService ds;
	@Autowired
	private ServiceHandler serviceHandler;

	// Handle a midnight reset action
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	private final Runnable midnightCountReset = createMidnightCountReset();

	@PostConstruct
	public void init()
	{
		long nextMidnight = Instant.now().plus(1, ChronoUnit.DAYS).truncatedTo(ChronoUnit.DAYS).toEpochMilli();
		long delayUntilStart = nextMidnight - Instant.now().toEpochMilli();
		scheduler.scheduleAtFixedRate(midnightCountReset, delayUntilStart, (long) 1, TimeUnit.MILLISECONDS);
		log.info("Inited a DatabaseService; setup a midnight reset; is ds set? {}", ds != null);
	}

	private Runnable createMidnightCountReset()
	{
		return new Runnable() {
			@Override
			public void run()
			{
				ds.resetLowCounts();
			}
		};
	}

	@RequestMapping("/api/endpointNames")
	public String endpointNames()
	{
		List<String> endpointList = ds.findAllEndpoints().stream().map(Endpoint::getSite).collect(Collectors.toList());
		return new Gson().toJson(endpointList);
	}

	@PostMapping("/api/endpoints")
	public int save(Endpoint endpoint)
	{
		return ds.saveEndpoint(endpoint);
	}

	@RequestMapping("/api/latencyCheck")
	public String latencyCheck()
	{
		List<String> serviceUrls = ds.findAllEndpoints().stream().map(Endpoint::getSite).collect(Collectors.toList());
		Map<String, Integer> latencyMap = serviceHandler.checkServices(serviceUrls);
		List<Entry<String, Integer>> topLatencies = latencyMap.entrySet().stream().filter(x -> x.getValue() >= ServiceHandler.MAX_LATENCY)
				.collect(Collectors.toList());
		ds.incrementBadPings(topLatencies);
		return new Gson().toJson(latencyMap);
	}

}
