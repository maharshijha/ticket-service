package com.walmart.ticketservice.exceptions;

import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NotFoundExceptionHandlerTest {

	@InjectMocks
	NotFoundExceptionHandler handler;
	
	private static final String developerTest="Requested Resource not avaiable";

	
	@Test
	public void testHandler(){
		NotFoundException exception=new NotFoundException("test", "test");
//		APIError error;
		Response resp=handler.toResponse(exception);
		assertTrue(((APIError)resp.getEntity()).getId().equals("test"));
		assertTrue(((APIError)resp.getEntity()).getClientText().equals("test"));
		assertTrue(((APIError)resp.getEntity()).getDeveloperText().equals(developerTest));

	}
}
