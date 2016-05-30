package com.walmart.ticketservice.service;

import javax.inject.Inject;
import javax.inject.Named;

import com.walmart.ticketservice.dao.HoldSeatsDao;
import com.walmart.ticketservice.dao.ReserveSeatsDao;
import com.walmart.ticketservice.dao.ResetServiceDao;
import com.walmart.ticketservice.dao.VacantSeatsDao;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the service class implementation.
 * 
 * @author Maharshi
 *
 */
@Named
public class TicketServiceImpl implements TicketService {

	@Inject
	VacantSeatsDao vacantSeatsDao;

	@Inject
	HoldSeatsDao holdSeatDao;

	@Inject
	ReserveSeatsDao reserveSeatsDao;
	
	@Inject
	ResetServiceDao resetServiceDao;

	private static int vanueId = 1;
	
	/*
	 * This method will take vanueLevel as param and return total available seats.
	 * @see com.walmart.ticketservice.service.TicketService#numSeatsAvailable(java.util.Optional)
	 */
	@Override
	public int numSeatsAvailable(Integer venueLevel) {

		int totalSeats = 0;
		/*
		 * Fetching the vanue detail from dao.
		 * Iterating over the vanue details and sending total available seats across the vanue. 
		 */
		for (VanueDetailsDTO VanueDetailsDTO : vacantSeatsDao.getVacantSeats(venueLevel)) {
			totalSeats += VanueDetailsDTO.getTotalAvailableSeats();
		}

		return totalSeats;

	}

	/*
	 * This method will find and hold the seats for given input params.
	 * @see com.walmart.ticketservice.service.TicketService#findAndHoldSeats(int, int, int, java.lang.String)
	 */
	@Override
	public HoldSeatDTO findAndHoldSeats(Integer numSeats, Integer minLevel,
			Integer maxLevel, String customerEmail) {
		/*
		 * Fatching the all available vanue details and pass it to hold seat dao so that it can act accordingly..
		 * 
		 */
		return holdSeatDao.holdSeats(numSeats, minLevel ,maxLevel , vanueId, vacantSeatsDao.getVacantSeats(null),
				customerEmail);

	}

	/*
	 * Reserving the seats for given hold id if seats are still available.
	 * @see com.walmart.ticketservice.service.TicketService#reserveSeats(int, java.lang.String)
	 */
	@Override
	public String reserveSeats(int seatHoldId, String customerEmail){
		
		return reserveSeatsDao.reserveSeats(seatHoldId, customerEmail);
	}

	@Override
	public void resetService() {
		resetServiceDao.resetService();		
	}

}
