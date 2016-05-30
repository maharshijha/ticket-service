package com.walmart;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TicketserviceApplication.class)
public class TicketserviceApplicationTests {

	@Inject
	TicketserviceApplication app;

	@Test
	public void testJmx() throws Exception {
		
	}
}
