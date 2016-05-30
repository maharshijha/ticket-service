package com.walmart.ticketservice.dao;

import java.util.List;

import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.VanueDetailsDTO;
/**
 * Interface for HoldSeatsDao.
 * @author Maharshi
 *
 */
public interface HoldSeatsDao {

	public HoldSeatDTO holdSeats(final int requestedSeats,
			final Integer minLevel, final Integer maxLevel, final int vanueID,
			final List<VanueDetailsDTO> vanueLevelDetails,
			final String customersEmail);

}
