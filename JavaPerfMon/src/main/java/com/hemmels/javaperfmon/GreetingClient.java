package com.hemmels.javaperfmon;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingClient {
	
	@RequestMapping("/")
	public String hello()
	{
		return "Hello World! This is me!";
	}
}
