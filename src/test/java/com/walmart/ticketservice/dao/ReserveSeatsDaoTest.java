package com.walmart.ticketservice.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.walmart.ticketservice.dao.impl.ReserveSeatsDaoImpl;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.HoldSeatDTO.SeatStatus;

/**
 * This is the Test class for ReserveSeatsDaoImpl.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ReserveSeatsDaoTest {
	@InjectMocks
	ReserveSeatsDaoImpl reserveSeatsDao;

	@Mock
	MongoTemplate mongoTemplate;

	private static final String NOT_FOUND_ERROR = "Ticket held is no more available or Invalid Seat Hold Id";

	/*
	 * Test class for reserveSeats happy path.
	 */
	@Test
	public void reserveSeatsHappyPath() {

		Query searchUserQuery = new Query(Criteria.where("_id").is(111));
		HoldSeatDTO holdSeatDTO = new HoldSeatDTO(0, 1111, "test", null, 1, 10,
				SeatStatus.HOLD.toString());

		when(mongoTemplate.findOne(searchUserQuery, HoldSeatDTO.class))
				.thenReturn(holdSeatDTO);

		String holdSeatDTOExpected = reserveSeatsDao.reserveSeats(111, "test");
		assertNotNull(holdSeatDTOExpected);

	}

	/*
	 * Test class for reserveSeats not found held seat.
	 */
	@Test
	public void reserveSeatsNoRecordFound() {

		Query searchUserQuery = new Query(Criteria.where("_id").is(111));

		when(mongoTemplate.findOne(searchUserQuery, HoldSeatDTO.class))
				.thenReturn(null);

		try {
			reserveSeatsDao.reserveSeats(111, "test");
			fail("Must throw notfound Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200001"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));
		}
	}

}
