package com.walmart.ticketservice.dao;

import com.walmart.ticketservice.exceptions.NotFoundException;

/**
 * Interface for ReserveSeatsDao.
 * 
 * @author Maharshi
 *
 */
public interface ReserveSeatsDao {

	public String reserveSeats(int seatHoldId, String customerEmail)
			throws NotFoundException;
}
