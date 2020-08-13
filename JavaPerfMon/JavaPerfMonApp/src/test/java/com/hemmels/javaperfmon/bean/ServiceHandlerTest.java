package com.hemmels.javaperfmon.bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;
import java.util.Map;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ServiceHandlerTest {

	ServiceHandler serviceHandler = new ServiceHandler();

	@Test
	void testCheckServices()
	{
		// We'll ping the live site in test, can't hurt.
		final String TEST_SITE = "www.google.co.uk";
		Condition<String> isTestSite = new Condition<>(x -> x.equals(TEST_SITE), "isTestSite");
		Condition<Integer> isUnder5s = new Condition<>(x -> x > 0 && x < 499, "isUnder5s");
		
		assertEquals(Collections.EMPTY_MAP, serviceHandler.checkServices(null));
		assertEquals(Collections.EMPTY_MAP, serviceHandler.checkServices(Collections.emptyList()));
		Map<String, Integer> map = serviceHandler.checkServices(Collections.singletonList(TEST_SITE));
		assertThat(map).hasSize(1).hasKeySatisfying(isTestSite).hasValueSatisfying(isUnder5s);
	}

}
