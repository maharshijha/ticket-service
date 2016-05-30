package com.walmart.ticketservice.dao.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.HoldSeatDTO.SeatStatus;
import com.walmart.ticketservice.model.Seat;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the MQ listener class.
 * 
 * @author Maharshi
 *
 */
public class ResetHoldSeatsDaoImpl implements MessageListener {

	MongoTemplate mongoTemplate;

	private final static String NOT_FOUND_ERROR = "JSON Convertor Error";

	/*
	 * This constructor will create object of listener and setting mongo templet
	 * passed from config.
	 */
	public ResetHoldSeatsDaoImpl(MongoTemplate mongoTemplate) {
		super();
		this.mongoTemplate = mongoTemplate;
	}

	/*
	 * This method will invoke by the delay message posted from the hold seat
	 * dao.
	 * 
	 * @see
	 * org.springframework.amqp.core.MessageListener#onMessage(org.springframework
	 * .amqp.core.Message)
	 */
	@Override
	public void onMessage(final Message message) {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Seat> seats;
		Iterator<Seat> it;
		Seat seat;

		/*
		 * finding for the object which have the hold id same as received
		 * message. checking still this seat is on hold or got booked. If seat
		 * is still on hold status then remove object and add those seats back
		 * to total.
		 */
		try {
			HoldSeatDTO holdSeatDTO = objectMapper.readValue(message.getBody(),
					HoldSeatDTO.class);
			Query searchHoldObject = new Query(Criteria.where("_id").is(
					holdSeatDTO.getSeatHoldId())).addCriteria(Criteria.where(
					"status").is(SeatStatus.HOLD.toString()));

			boolean currentStatus = mongoTemplate.exists(searchHoldObject,
					HoldSeatDTO.class);
			if (currentStatus) {
				mongoTemplate.remove(holdSeatDTO);
				seats = holdSeatDTO.getSeatsDetails();
				it = seats.iterator();
				while (it.hasNext()) {
					seat = ((Seat) it.next());
					Query searchVanueDetailsDTO = new Query(Criteria.where(
							"_id").is(seat.getLevelId()));
					VanueDetailsDTO VanueDetailsDTO = mongoTemplate.findOne(
							searchVanueDetailsDTO, VanueDetailsDTO.class);
					VanueDetailsDTO.setTotalAvailableSeats(VanueDetailsDTO
							.getTotalAvailableSeats() + seat.getNoOfSeat());
					mongoTemplate.save(VanueDetailsDTO);
				}
			}
		} catch (IOException e) {
			throw new NotFoundException("200005", NOT_FOUND_ERROR);

		}

	}
}
