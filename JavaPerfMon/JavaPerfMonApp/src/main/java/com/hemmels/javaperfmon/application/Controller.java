package com.hemmels.javaperfmon.application;

import java.util.List;
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

	@PostConstruct
	public void init()
	{
		log.info("Inited a DatabaseService; is ds set? {}", ds != null);
	}

	@RequestMapping("/api")
	public String hello()
	{
		return "This is the correct site/API, you need to visit /endpoints etc to view the data.";
	}

	@RequestMapping("/api/endpointNames")
	public String endpointNames()
	{
		List<String> endpointList = ds.findAllEndpoints().stream().map(Endpoint::getSite).collect(Collectors.toList());
		return new Gson().toJson(endpointList);
	}

	@RequestMapping("/api/endpoints")
	public String endpoints()
	{
		List<Endpoint> endpointList = ds.findAllEndpoints().stream().collect(Collectors.toList());
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
		return new Gson().toJson(serviceHandler.checkServices(serviceUrls));
	}

}
