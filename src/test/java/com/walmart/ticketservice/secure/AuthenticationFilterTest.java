package com.walmart.ticketservice.secure;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * This is the Test class for AuthenticationFilter.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFilterTest {
	
	@Mock
	ContainerRequestContext request;
	
	@InjectMocks
	AuthenticationFilter filter;
	
	@Mock
	 UriInfo uriInfo;
	
    private static final String AUTHORIZATION_EXCEPTION_SWAGGER = "swagger.json";

	
	/*
	 * This Test class will test the case when API KEY passed not matched.
	 */
	@Test
	public void filterTestNegative(){
		MultivaluedMap<String,String> map=new MultivaluedHashMap<String,String>();
		map.add("API-KEY", "ABCD");
		when(uriInfo.getPath()).thenReturn("Test");
		when(request.getHeaders()).thenReturn(map);
		try {
			filter.filter(request);
			Mockito.verify(request, Mockito.times(1)).abortWith(any(Response.class));
		} catch (IOException e) {
			fail("Exception Thrown");
		}
		
	}
	
	/*
	 * This Test class will test the case when API KEY passed matched.
	 */
	@Test
	public void filterTestPositive(){
		MultivaluedMap<String,String> map=new MultivaluedHashMap<String,String>();
		map.add("API-KEY", "WALMART");
		when(uriInfo.getPath()).thenReturn("Test");
		when(request.getHeaders()).thenReturn(map);
		try {
			filter.filter(request);
			Mockito.verify(request, Mockito.times(0)).abortWith(any(Response.class));
		} catch (IOException e) {
			fail("Exception Thrown");
		}
		
	}
	/*
	 * This Test class will test the case when no API KEY.
	 */
	@Test
	public void filterTestNotPassed(){
		MultivaluedMap<String,String> map=new MultivaluedHashMap<String,String>();
	
		when(uriInfo.getPath()).thenReturn("Test");
		when(request.getHeaders()).thenReturn(map);
		try {
			filter.filter(request);
			Mockito.verify(request, Mockito.times(1)).abortWith(any(Response.class));
		} catch (IOException e) {
			fail("Exception Thrown");
		}
		
	}
	
	/*
	 * This Test class will test the case when Swagger.json requested.
	 */
	@Test
	public void filterTestSwaggerPassed(){
		MultivaluedMap<String,String> map=new MultivaluedHashMap<String,String>();
	
		when(uriInfo.getPath()).thenReturn(AUTHORIZATION_EXCEPTION_SWAGGER);
		when(request.getHeaders()).thenReturn(map);
		try {
			filter.filter(request);
			Mockito.verify(request, Mockito.times(0)).abortWith(any(Response.class));
		} catch (IOException e) {
			fail("Exception Thrown");
		}
		
	}
	
}
