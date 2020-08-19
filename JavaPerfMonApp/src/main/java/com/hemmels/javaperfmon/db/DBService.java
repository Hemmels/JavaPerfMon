package com.hemmels.javaperfmon.db;

import java.util.List;
import java.util.Map.Entry;

import org.jooq.generated.tables.pojos.Endpoint;

public interface DBService {

	public List<Endpoint> findAllEndpoints(Boolean enabled);

	public Endpoint findEndpointById(int id);

	public int saveEndpoint(Endpoint endpoint);

	public void incrementBadPings(List<Entry<String, Integer>> topLatencies);

	public void resetLowCounts();

	public void enableAllEndpoints(boolean enabledFlag);

	public int removeEndpoint(String endpointUrl);
}
