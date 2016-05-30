package com.walmart.ticketservice.rest.resources.v1;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.Response;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.requests.FindAndHoldSeatsRequest;
import com.walmart.ticketservice.requests.ReserveSeatsRequest;
import com.walmart.ticketservice.responses.ReserveSeatsResponse;
import com.walmart.ticketservice.responses.SeatsAvailableResponse;
import com.walmart.ticketservice.service.TicketService;

/**
 * This is the test class for the TicketServiceResource.
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TicketServiceResourceTest {

	@InjectMocks
	TicketServiceResource resource;
	
	@Mock
	TicketService service;
	
	/**
	 * This test will perform the test on ReouceFindAndHoldSeats resource.
	 */
	@Test
	public void testReouceFindAndHoldSeats(){
		FindAndHoldSeatsRequest request=new FindAndHoldSeatsRequest();
		request.setCustomerEmail("test");
		request.setNumSeats(2000);
		HoldSeatDTO dto=new HoldSeatDTO(0, 2, "test", null, 1, 2000, "HOLD");
		when(service.findAndHoldSeats(200, 0, 0, "test")).thenReturn(dto);
		Response resp=resource.findAndHoldSeats(request, "1");
		assertTrue(resp.getStatus()==201);
	}
	
	/**
	 * This test will perform the test on total number of seats resource.
	 */
	@Test
	public void testNumSeatsAvailable(){
		when(service.numSeatsAvailable(1)).thenReturn(1000);
		Response respExpected=resource.numSeatsAvailable(1, 1);
		assertTrue(respExpected.getStatus()==200);
		assertTrue(((SeatsAvailableResponse)respExpected.getEntity()).getTotalAvailableSeats()==1000);

	}
	
	/**
	 * This test will perform the test on reserveSeats resource.
	 */
	@Test
	public void testReserveSeats(){
		ReserveSeatsRequest request=new ReserveSeatsRequest();
		request.setCustomerEmail("test");
		request.setSeatHoldId(11);
		when(service.reserveSeats(11, "test")).thenReturn("11");
		Response respExpected=resource.reserveSeats(request, "1");
		assertTrue(respExpected.getStatus()==200);
		assertTrue(((ReserveSeatsResponse)respExpected.getEntity()).getBookingReferenceCode().equals("11"));

	}
	
	/**
	 * This test will perform the test on resetService resource.
	 */
	@Test
	public void testResetService(){
		
		Response respExpected=resource.resetService();
		assertTrue(respExpected.getStatus()==204);

	}
	
}
