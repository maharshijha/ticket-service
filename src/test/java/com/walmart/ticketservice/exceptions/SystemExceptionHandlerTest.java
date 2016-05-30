package com.walmart.ticketservice.exceptions;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SystemExceptionHandlerTest {

	@InjectMocks
	SystemExceptionHandler handler;
	
	@Test
	public void testHandler(){
//		APIError error;
		RuntimeException exeception=new RuntimeException("Test");
		Response resp=handler.toResponse(exeception);
		assertTrue(resp.getStatus()==Status.SERVICE_UNAVAILABLE.getStatusCode());

		assertTrue(((APIError)resp.getEntity()).getId().equals("200002"));
		assertTrue(((APIError)resp.getEntity()).getClientText().equals("System is not available currently."));
		assertTrue(((APIError)resp.getEntity()).getDeveloperText().equals("Test"));

	}
}
