package com.walmart.ticketservice.mongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * Marker interface for the VanueDetailsMongoRepository.
 * 
 * @author Maharshi
 *
 */
@Repository
public interface VanueDetailsMongoRepository extends
		MongoRepository<VanueDetailsDTO, String> {

}