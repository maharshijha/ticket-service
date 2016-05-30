package com.walmart.ticketservice.dao.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.walmart.ticketservice.dao.ReserveSeatsDao;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.HoldSeatDTO.SeatStatus;

/**
 * This is the DAO class for booking the held seats.
 * 
 * @author Maharshi
 *
 */
@Named
public class ReserveSeatsDaoImpl implements ReserveSeatsDao {

	@Inject
	MongoTemplate mongoTemplate;

	private final static String NOT_FOUND_ERROR = "Ticket held is no more available or Invalid Seat Hold Id";

	/*
	 * This will confirm the booking of held seats if the held seats are not
	 * expired.
	 * 
	 * @see com.walmart.ticketservice.dao.ReserveSeatsDao#reserveSeats(int,
	 * java.lang.String)
	 */
	public String reserveSeats(final int seatHoldId, final String customerEmail) {

		int bookinfRefNo = (int) (Math.random() * 1000000);

		Query searchUserQuery = new Query(Criteria.where("_id").is(seatHoldId));
		HoldSeatDTO holdSeatDTO = mongoTemplate.findOne(searchUserQuery,
				HoldSeatDTO.class);
		if (null != holdSeatDTO) {
			holdSeatDTO.setStatus(SeatStatus.BOOKED.toString());
			holdSeatDTO.setBookinfRefNo(bookinfRefNo);
			holdSeatDTO.setCustomerEmail(customerEmail);
			mongoTemplate.save(holdSeatDTO);
			return holdSeatDTO.getBookinfRefNo() + "";
		} else {
			throw new NotFoundException("200001", NOT_FOUND_ERROR);
		}

	}

}
