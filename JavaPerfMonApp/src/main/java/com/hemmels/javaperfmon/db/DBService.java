package com.hemmels.javaperfmon.db;

import java.util.List;

import org.jooq.generated.tables.pojos.Endpoint;

public interface DBService {

	public List<Endpoint> findAllEndpoints();

	public Endpoint findEndpointById(int id);

	public int saveEndpoint(Endpoint endpoint);
}
