package com.hemmels.javaperfmon.bean;

import static org.springframework.util.CollectionUtils.isEmpty;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceHandler {

	public static final Integer MAX_LATENCY = 300;
	private static final Integer TIMEOUT = 5000;

	public Map<String, Integer> checkServices(List<String> urls) {
		if (isEmpty(urls)) {
			return Collections.emptyMap();
		}
		Map<String, Integer> latencies = new HashMap<>(urls.size());
		for (String url : urls) {
			latencies.put(url, checkLatency(url));
		}
		return latencies;
	}

	private int checkLatency(String url) {
		int latency = MAX_LATENCY;
		URLConnection conn = getConnectionForUrl(url);
		if (conn == null) {
			log.debug("Connection could not be obtained for {}", url);
			return latency;
		}
		
		long startTime = System.currentTimeMillis();
		try (InputStream in = conn.getInputStream()) {
			// This cast is safe, because of the timeouts above.
			latency = (int) (System.currentTimeMillis() - startTime);
		} catch (IOException e) {
			log.error("{} thrown checking latency of {}", e.getClass().getName(), url, e);
		}

		log.debug("Latency for {}: {}", url, latency);
		return latency;
	}
	
	private URLConnection getConnectionForUrl(String url) {
		URLConnection con = null;
		try {
			if (!url.startsWith("http")) {
				url = "http://" + url;
			}
			con = new URL(url).openConnection();
		} catch (IOException e) {
			log.error("{} thrown opening connection to {}", e.getClass().getName(), url, e);
			return con;
		}
		con.setConnectTimeout(TIMEOUT);
		con.setReadTimeout(TIMEOUT);
		return con;
	}
}
