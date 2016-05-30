package com.walmart.ticketservice.model;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HoldSeatDTOTest {
	
	@Test
	public void testBean(){
		HoldSeatDTO dto=new HoldSeatDTO();
		dto.setBookinfRefNo(1);
		dto.setCustomerEmail("test");
		dto.setNoOfSeats(2);
		dto.setSeatHoldId(2);
		dto.setStatus("test");
		dto.setVanue(2);
		dto.setVanueId(2);
		
		assertTrue(dto.getBookinfRefNo()==1);
		assertTrue(dto.getCustomerEmail().equals("test"));
		assertTrue(dto.getNoOfSeats()==2);
		assertTrue(dto.getSeatHoldId()==2);
		assertTrue(dto.getStatus().equals("test"));
		
	}

}
