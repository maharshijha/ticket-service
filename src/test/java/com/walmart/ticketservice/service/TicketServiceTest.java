package com.walmart.ticketservice.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.walmart.ticketservice.dao.HoldSeatsDao;
import com.walmart.ticketservice.dao.ReserveSeatsDao;
import com.walmart.ticketservice.dao.ResetServiceDao;
import com.walmart.ticketservice.dao.VacantSeatsDao;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the Test class for TicketService.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class TicketServiceTest {
	
	@InjectMocks
	TicketServiceImpl impl;
	
	@Mock
	VacantSeatsDao vacantSeatsDao;

	@Mock
	HoldSeatsDao holdSeatDao;

	@Mock
	ReserveSeatsDao reserveSeatsDao;

	@Mock
	ResetServiceDao resetServiceDao;
	
	/*
	 * Test class for reserveSeats.
	 */
	@Test
	public void reserveSeats() {
		
		when(reserveSeatsDao.reserveSeats(1, "test")).thenReturn("Test");
		String resp=impl.reserveSeats(1, "test");
		assertTrue(resp.equals("Test"));
	}

	/*
	 * Test class for findAndHoldSeats.
	 */
	@Test
	public void findAndHoldSeats() {
		HoldSeatDTO dto=new HoldSeatDTO(0, 0, "test", null, 1, 1, "HOLD");
		
		when(holdSeatDao.holdSeats(100, 1, 1, 1, null, "test")).thenReturn(dto);
		when(vacantSeatsDao.getVacantSeats(null)).thenReturn(null);
		HoldSeatDTO dtoExpected=impl.findAndHoldSeats(100, 1, 1, "test");
		assertTrue(dtoExpected.toString().equals(dto.toString()));
	}
	/*
	 * Test class for vacantSeats.
	 */
	@Test
	public void vacantSeats() {
		List<VanueDetailsDTO> vanueDetails=new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto1=new VanueDetailsDTO(1, "Orchestra", "$100", 25 * 50, 1);
		VanueDetailsDTO dto2=new VanueDetailsDTO(2, "Main", "$75", 20 * 100, 1);
		VanueDetailsDTO dto3=new VanueDetailsDTO(3, "Balcony 1", "$50", 15 * 100, 1);
		VanueDetailsDTO dto4=new VanueDetailsDTO(4, "Balcony 2", "$40", 15 * 100, 1);
		vanueDetails.add(dto1);
		vanueDetails.add(dto2);
		vanueDetails.add(dto3);
		vanueDetails.add(dto4);
		
		
		when(vacantSeatsDao.getVacantSeats(null)).thenReturn(vanueDetails);
		int total=impl.numSeatsAvailable(null);
		assertTrue(total==6250);
	}
	
	/*
	 * Test Class for reset service.
	 */
	@Test
	public void resetService(){
		impl.resetService();
	}
}
