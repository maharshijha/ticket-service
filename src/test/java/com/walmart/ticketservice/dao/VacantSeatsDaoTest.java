package com.walmart.ticketservice.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import com.walmart.ticketservice.dao.impl.VacantSeatsDaoImpl;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the Test class for VacantSeatsDao.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class VacantSeatsDaoTest {

	@InjectMocks
	VacantSeatsDaoImpl vacantSeatsDaoImpl;

	@Mock
	MongoTemplate mongoTemplate;
	
	private static final String NOT_FOUND_ERROR = "Level Not Available";
	

	/*
	 * This Test is for fetching all details when no level id preference passed.
	 */
	@Test
	public void testVacantSeatsDaoHappy() {

		List<VanueDetailsDTO> vanueDetails=new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto1=new VanueDetailsDTO(1, "Orchestra", "$100", 25 * 50, 1);
		VanueDetailsDTO dto2=new VanueDetailsDTO(2, "Main", "$75", 20 * 100, 1);
		VanueDetailsDTO dto3=new VanueDetailsDTO(3, "Balcony 1", "$50", 15 * 100, 1);
		VanueDetailsDTO dto4=new VanueDetailsDTO(4, "Balcony 2", "$40", 15 * 100, 1);
		vanueDetails.add(dto1);
		vanueDetails.add(dto2);
		vanueDetails.add(dto3);
		vanueDetails.add(dto4);
		List<VanueDetailsDTO> vanueDetailsExpected;
		when(mongoTemplate.findAll(VanueDetailsDTO.class)).thenReturn(vanueDetails);
		vanueDetailsExpected=vacantSeatsDaoImpl.getVacantSeats(null);
		assertTrue(vanueDetails.toString().equals(vanueDetailsExpected.toString()));
		
	}
	

	/*
	 * This Test is for fetching all details whenlevel id preference passed.
	 */
	@Test
	public void testVacantSeatsDaoLevelHappy() {

		Query searchVenueQuery = new Query(Criteria.where("_id").is(
				1));
		List<VanueDetailsDTO> vanueDetails=new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto1=new VanueDetailsDTO(1, "Orchestra", "$100", 25 * 50, 1);
		
		
		when(mongoTemplate.findOne(searchVenueQuery,VanueDetailsDTO.class)).thenReturn(dto1);
		vanueDetails=vacantSeatsDaoImpl.getVacantSeats(1);
		assertTrue(vanueDetails.get(0).equals(dto1));
		
	}
	/*
	 * This Test is for when level passed and no data found for that level.
	 */
	@Test
	public void testVacantSeatsDaoNoDataForLevel() {

		Query searchVenueQuery = new Query(Criteria.where("_id").is(
				1));
		
		
		
		when(mongoTemplate.findOne(searchVenueQuery,VanueDetailsDTO.class)).thenReturn(null);
		try{
			vacantSeatsDaoImpl.getVacantSeats(1);
			fail("Must throw notfound Exception");
		}
		catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200006"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));
		}
		
	}
	/*
	 * This Test is for when  no data found.
	 */
	@Test
	public void testVacantSeatsDaoNoData() {
		
		
		when(mongoTemplate.findAll(VanueDetailsDTO.class)).thenReturn(null);
		try{
			vacantSeatsDaoImpl.getVacantSeats(null);
			fail("Must throw notfound Exception");
		}
		catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200006"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));
		}
		
	}

}
