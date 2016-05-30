package com.walmart.ticketservice.dao;

import java.util.List;

import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the interface for the VacantSeatsDao.
 * 
 * @author Maharshi
 *
 */
public interface VacantSeatsDao {
	public List<VanueDetailsDTO> getVacantSeats(Integer venueLevel);
}
