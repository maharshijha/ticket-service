package com.walmart.ticketservice.dao;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.core.Message;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.walmart.ticketservice.dao.impl.ResetHoldSeatsDaoImpl;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.HoldSeatDTO.SeatStatus;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the Test class for ResetHoldSeatDao.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetHoldSeatDaoTest {

	@InjectMocks
	ResetHoldSeatsDaoImpl dao;

	@Mock
	MongoTemplate mongoTemplate;

	/*
	 * This will test the path when held tickets are not Confirmed and will remove from collection.
	 */
	@Test
	public void onMessageTest() {
		Query searchVanueDetailsDTO = new Query(Criteria.where("_id").is(1));
		Query searchHoldObject = new Query(Criteria.where("_id").is(1111))
				.addCriteria(Criteria.where("status").is(
						SeatStatus.HOLD.toString()));
		String body = "{ \"bookinfRefNo\": 0, \"seatHoldId\": 1111, \"customerEmail\": \"maharshi@hotmail.com\", \"seatsDetails\": [ { \"noOfSeat\": 1250, \"levelId\": 1, \"levenName\": \"Orchestra\" }], \"vanueId\": 1, \"noOfSeats\": 1250, \"status\": \"HOLD\", \"vanue\": 1 }";
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "test", "100$", 100, 1);

		when(mongoTemplate.exists(searchHoldObject, HoldSeatDTO.class))
				.thenReturn(true);
		when(
				mongoTemplate.findOne(searchVanueDetailsDTO,
						VanueDetailsDTO.class)).thenReturn(dto);

		Message message = new Message(body.getBytes(), null);
		dao.onMessage(message);
		Mockito.verify(mongoTemplate, Mockito.times(1)).save(
				any(VanueDetailsDTO.class));
		Mockito.verify(mongoTemplate, Mockito.times(1)).remove(
				any(HoldSeatDTO.class));

	}
	
	/*
	 * This will test the path when held tickets are Confirmed and drop the message.
	 */
	@Test
	public void onMessageTestHeldSeatNotAvailable() {
		Query searchVanueDetailsDTO = new Query(Criteria.where("_id").is(1));
		Query searchHoldObject = new Query(Criteria.where("_id").is(1111))
				.addCriteria(Criteria.where("status").is(
						SeatStatus.HOLD.toString()));
		String body = "{ \"bookinfRefNo\": 0, \"seatHoldId\": 1111, \"customerEmail\": \"maharshi@hotmail.com\", \"seatsDetails\": [ { \"noOfSeat\": 1250, \"levelId\": 1, \"levenName\": \"Orchestra\" }], \"vanueId\": 1, \"noOfSeats\": 1250, \"status\": \"HOLD\", \"vanue\": 1 }";
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "test", "100$", 100, 1);

		when(mongoTemplate.exists(searchHoldObject, HoldSeatDTO.class))
				.thenReturn(false);
		when(
				mongoTemplate.findOne(searchVanueDetailsDTO,
						VanueDetailsDTO.class)).thenReturn(dto);

		Message message = new Message(body.getBytes(), null);
		dao.onMessage(message);
		Mockito.verify(mongoTemplate, Mockito.times(0)).save(
				any(VanueDetailsDTO.class));
		Mockito.verify(mongoTemplate, Mockito.times(0)).remove(
				any(HoldSeatDTO.class));

	}

}
