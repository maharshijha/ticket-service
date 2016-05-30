package com.walmart.ticketservice.mongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.walmart.ticketservice.model.HoldSeatDTO;

/**
 * Marker interface for the ReserveTicketMongoRepository.
 * 
 * @author Maharshi
 *
 */
@Repository
public interface ReserveTicketMongoRepository extends
		MongoRepository<HoldSeatDTO, String> {

}
