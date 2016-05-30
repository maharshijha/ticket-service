package com.walmart.ticketservice.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.walmart.ticketservice.dao.impl.ResetServiceDaoImpl;
import com.walmart.ticketservice.mongo.repo.VanueDetailsMongoRepository;

/**
 * This is the Test class for ResetServiceDao.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ResetServiceDaoTest {
	@InjectMocks
	ResetServiceDaoImpl resetServiceDao;
	
	@Mock
	MongoTemplate mongoTemplate;
	
	@Mock
	VanueDetailsMongoRepository vanueRepo;
	
	/*
	 * This test is for the Reset Service.
	 */
	@Test
	public void resetServiceTest(){
		resetServiceDao.resetService();
	}

}
