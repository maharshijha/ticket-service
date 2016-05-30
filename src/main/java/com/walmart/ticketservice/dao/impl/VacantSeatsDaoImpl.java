package com.walmart.ticketservice.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.walmart.ticketservice.dao.VacantSeatsDao;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the DAO for fetching the vacant seats.
 * 
 * @author MAHARSHI
 *
 */

@Named
public class VacantSeatsDaoImpl implements VacantSeatsDao {

	@Inject
	MongoTemplate mongoTemplate;

	private static final String NOT_FOUND_ERROR = "Level Not Available";

	/*
	 * This method will accept the vanue level and fetch the vanue details based
	 * on input.
	 * 
	 * @see
	 * com.walmart.ticketservice.dao.VacantSeatsDao#getVacantSeats(java.util
	 * .Optional)
	 */
	public List<VanueDetailsDTO> getVacantSeats(final Integer venueLevel) {

		List<VanueDetailsDTO> VanueDetailsDTOList;
		/*
		 * If vanue level is present then fetch the vanue details for given
		 * level. Else find all.
		 */
		if (null != venueLevel) {
			Query searchVenueQuery = new Query(Criteria.where("_id").is(
					venueLevel));

			VanueDetailsDTO vanueDetails = mongoTemplate.findOne(
					searchVenueQuery, VanueDetailsDTO.class);
			if (null != vanueDetails) {
				VanueDetailsDTOList = new ArrayList<VanueDetailsDTO>();
				VanueDetailsDTOList.add(vanueDetails);
			} else {
				throw new NotFoundException("200006", NOT_FOUND_ERROR);
			}
		} else {
			VanueDetailsDTOList = mongoTemplate.findAll(VanueDetailsDTO.class);
			if (null == VanueDetailsDTOList) {
				throw new NotFoundException("200006", NOT_FOUND_ERROR);
			}
		}
		return VanueDetailsDTOList;
	}

}
