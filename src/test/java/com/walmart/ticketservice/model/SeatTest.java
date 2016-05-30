package com.walmart.ticketservice.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SeatTest {

	@Test
	public void testBean(){
		Seat seat=new Seat();
		seat.setLevelId(1);
		seat.setLevenName("test");
		seat.setNoOfSeat(2);
		assertTrue(seat.getLevelId()==1);
		assertTrue(seat.getLevenName().equals("test"));
		assertTrue(seat.getNoOfSeat()==2);
		
		
	}
}
