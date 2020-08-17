package com.hemmels.javaperfmon.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.jooq.generated.tables.pojos.Endpoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.google.gson.Gson;
import com.hemmels.javaperfmon.bean.ServiceHandler;
import com.hemmels.javaperfmon.db.DBService;
import com.hemmels.javaperfmon.db.H2Service;

@ContextConfiguration(classes = {H2Service.class})
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ControllerTest {

	@Mock
	private DBService ds;
	@Mock
	private ServiceHandler serviceHandler;

	@InjectMocks
	private Controller controller;

	private static final String SITE_NAME_1 = "somesite.co.uk";
	private static final String SITE_NAME_2 = "disabled.no";
	private static final int ENDPOINT_LATENCY = 42;

	@BeforeEach
	void init()
	{
		when(ds.findAllEndpoints(true)).thenReturn(makeEndpointList(true));
		when(serviceHandler.checkServices(anyList())).thenReturn(Collections.singletonMap("key", 42));
	}

	@Test
	void testEndpointNames()
	{
		String result = controller.endpointNames();
		assertThat(result).contains("[", "]", SITE_NAME_1, SITE_NAME_2);
		assertTrue(isJSONValid(result));
	}

	@Test
	void testLatencyCheck()
	{
		String result = controller.latencyCheck();
		assertThat(result).contains("{", "}", ENDPOINT_LATENCY + "");
		assertTrue(isJSONValid(result));
	}

	private List<Endpoint> makeEndpointList(Boolean enabled)
	{
		List<Endpoint> list = List.of(new Endpoint(1, SITE_NAME_1, true, 0, LocalDateTime.now().minusMonths(3).minusDays(2)),
				new Endpoint(2, SITE_NAME_2, false, 0, LocalDateTime.now().minusMinutes(15)));

		if (enabled != null) {
			if (enabled) {
				list.remove(1);
			} else {
				list.remove(0);
			}
		}
		return list;
	}

	public static boolean isJSONValid(String jsonInString)
	{
		try {
			new Gson().fromJson(jsonInString, Object.class);
			return true;
		} catch (com.google.gson.JsonSyntaxException ex) {
			return false;
		}
	}
}
