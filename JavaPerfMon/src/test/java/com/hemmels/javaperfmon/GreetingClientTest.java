package com.hemmels.javaperfmon;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class GreetingClientTest {
	
	private GreetingClient client = new GreetingClient();

	@Test
    void testHello() {
        assertEquals("Hello World! This is me!", client.hello());
    }
	
}
