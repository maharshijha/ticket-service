package com.walmart.ticketservice.dao.impl;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.walmart.RabbitMqConfig;
import com.walmart.ticketservice.dao.ResetServiceDao;
import com.walmart.ticketservice.model.VanueDetailsDTO;
import com.walmart.ticketservice.mongo.repo.VanueDetailsMongoRepository;

@Named
public class ResetServiceDaoImpl implements ResetServiceDao {

	@Inject
	MongoTemplate mongoTemplate;

	@Inject
	RabbitMqConfig rabbitMqConfig;

	@Inject
	VanueDetailsMongoRepository vanueRepo;

	@Override
	public void resetService() {
		mongoTemplate.dropCollection("vanuedetails");
		mongoTemplate.dropCollection("holdSeats");
		vanueRepo.deleteAll();

		vanueRepo.save(new VanueDetailsDTO(1, "Orchestra", "$100", 25 * 50, 1));
		vanueRepo.save(new VanueDetailsDTO(2, "Main", "$75", 20 * 100, 1));
		vanueRepo.save(new VanueDetailsDTO(3, "Balcony 1", "$50", 15 * 100, 1));
		vanueRepo.save(new VanueDetailsDTO(4, "Balcony 2", "$40", 15 * 100, 1));

	}

}
