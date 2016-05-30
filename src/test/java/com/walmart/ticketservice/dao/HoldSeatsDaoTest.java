package com.walmart.ticketservice.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.rabbitmq.client.Channel;
import com.walmart.RabbitMqConfig;
import com.walmart.ticketservice.dao.impl.HoldSeatsDaoImpl;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.VanueDetailsDTO;

/**
 * This is the Junit test class for HoldSeatsDaoImpl.
 * 
 * @author Maharshi
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class HoldSeatsDaoTest {

	@InjectMocks
	HoldSeatsDaoImpl holdSeatsDao;

	@Mock
	MongoTemplate mongoTemplet;

	@Mock
	RabbitMqConfig rabbitMqConfig;

	@Mock
	Channel channel;

	private final String DESTINATION_QUEUE = "direct";

	private final String NOT_FOUND_ERROR = "No More Seats aviable currently";

	private final String SYSTEM_ERROR = "MQ System is not avaiable currently";

	/**
	 * Test class for Hold Seats happy path when No level pref send.
	 */
	@Test
	public void holdSeatsTestWithoutLevelPref() {
		int requestedSeats = 10;
		HoldSeatDTO holdSeatDTO;
		int vanueID = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 20000, 1);
		vanueLevelDetails.add(dto);
		String customersEmail = "test";
		org.springframework.amqp.rabbit.connection.Connection conn = mock(Connection.class);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", "");
		args.put("x-dead-letter-routing-key", DESTINATION_QUEUE);
		args.put("x-message-ttl", 120000);

		when(rabbitMqConfig.connectionFactory()).thenReturn(connectionFactory);
		when(connectionFactory.createConnection()).thenReturn(conn);
		when(conn.createChannel(false)).thenReturn(channel);
		holdSeatDTO = holdSeatsDao.holdSeats(requestedSeats, null, null,
				vanueID, vanueLevelDetails, customersEmail);
		assertNotNull(holdSeatDTO);
		assertNotNull(holdSeatDTO.getSeatHoldId());
		assertNotNull(holdSeatDTO.getNoOfSeats());
		assertTrue(holdSeatDTO.getCustomerEmail().equals(customersEmail));

		assertNotNull(holdSeatDTO.getSeatsDetails());
		assertTrue(holdSeatDTO.getSeatsDetails().size() > 0);

	}

	/**
	 * Test class for Hold Seats happy path when Min level pref send.
	 */
	@Test
	public void holdSeatsTestMinLevelPref() {
		int requestedSeats = 10;
		Integer minLevel = 2;
		HoldSeatDTO holdSeatDTO;
		int vanueID = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 20000, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 20000, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);

		String customersEmail = "test";
		org.springframework.amqp.rabbit.connection.Connection conn = mock(Connection.class);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", "");
		args.put("x-dead-letter-routing-key", DESTINATION_QUEUE);
		args.put("x-message-ttl", 120000);

		when(rabbitMqConfig.connectionFactory()).thenReturn(connectionFactory);
		when(connectionFactory.createConnection()).thenReturn(conn);
		when(conn.createChannel(false)).thenReturn(channel);
		holdSeatDTO = holdSeatsDao.holdSeats(requestedSeats,
				minLevel, null, vanueID, vanueLevelDetails,
				customersEmail);
		assertNotNull(holdSeatDTO);
		assertNotNull(holdSeatDTO.getSeatHoldId());
		assertNotNull(holdSeatDTO.getNoOfSeats());
		assertNotNull(holdSeatDTO.getSeatsDetails());
		assertTrue(holdSeatDTO.getSeatsDetails().size() > 0);
		assertTrue(holdSeatDTO.getCustomerEmail().equals(customersEmail));

		assertTrue(holdSeatDTO.getSeatsDetails().get(0).getLevelId() >= minLevel);

	}

	/**
	 * Test class for Hold Seats happy path when Max level pref send.
	 */
	@Test
	public void holdSeatsTestMaxLevelPref() {
		int requestedSeats = 10;
		Integer maxLevel = 2;
		HoldSeatDTO holdSeatDTO;
		int vanueID = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 20000, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 20000, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);
		String customersEmail = "test";
		org.springframework.amqp.rabbit.connection.Connection conn = mock(Connection.class);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", "");
		args.put("x-dead-letter-routing-key", DESTINATION_QUEUE);
		args.put("x-message-ttl", 120000);

		when(rabbitMqConfig.connectionFactory()).thenReturn(connectionFactory);
		when(connectionFactory.createConnection()).thenReturn(conn);
		when(conn.createChannel(false)).thenReturn(channel);
		holdSeatDTO = holdSeatsDao.holdSeats(requestedSeats, null,
				maxLevel, vanueID, vanueLevelDetails,
				customersEmail);
		assertNotNull(holdSeatDTO);
		assertNotNull(holdSeatDTO.getSeatHoldId());
		assertNotNull(holdSeatDTO.getNoOfSeats());
		assertNotNull(holdSeatDTO.getSeatsDetails());
		assertTrue(holdSeatDTO.getSeatsDetails().size() > 0);
		assertTrue(holdSeatDTO.getCustomerEmail().equals(customersEmail));

		assertTrue(holdSeatDTO.getSeatsDetails().get(0).getLevelId() < maxLevel);

	}

	/**
	 * Test class for Hold Seats happy path when Max and Min levels pref send.
	 */
	@Test
	public void holdSeatsTestMaxMinLevelPref() {
		int requestedSeats = 10;
		Integer maxLevel = 2;
		Integer minLevel = 1;

		HoldSeatDTO holdSeatDTO;
		int vanueID = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 20000, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 20000, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);
		String customersEmail = "test";
		org.springframework.amqp.rabbit.connection.Connection conn = mock(Connection.class);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", "");
		args.put("x-dead-letter-routing-key", DESTINATION_QUEUE);
		args.put("x-message-ttl", 120000);

		when(rabbitMqConfig.connectionFactory()).thenReturn(connectionFactory);
		when(connectionFactory.createConnection()).thenReturn(conn);
		when(conn.createChannel(false)).thenReturn(channel);
		holdSeatDTO = holdSeatsDao.holdSeats(requestedSeats,
				minLevel, maxLevel, vanueID,
				vanueLevelDetails, customersEmail);
		assertNotNull(holdSeatDTO);
		assertNotNull(holdSeatDTO.getSeatHoldId());
		assertNotNull(holdSeatDTO.getNoOfSeats());
		assertNotNull(holdSeatDTO.getSeatsDetails());
		assertTrue(holdSeatDTO.getCustomerEmail().equals(customersEmail));
		assertTrue(holdSeatDTO.getSeatsDetails().size() > 0);
		assertTrue(holdSeatDTO.getSeatsDetails().get(0).getLevelId() < maxLevel
				&& holdSeatDTO.getSeatsDetails().get(0).getLevelId() >= minLevel);

	}

	/**
	 * Test class for Hold Seats Error path when No Vanue Details found.
	 */
	@Test
	public void holdSeatsNoVanue() {
		int requestedSeats = 10;
		Integer maxLevel = 2;
		Integer minLevel = 1;

		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();

		try {
			holdSeatsDao.holdSeats(requestedSeats, minLevel,
					maxLevel, 1, vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when Invalid No Of Seats Details
	 * found.
	 */
	@Test
	public void holdSeatsInvalidNoOfSeats() {
		int requestedSeats = 0;
		Integer maxLevel = 2;
		Integer minLevel = 1;

		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 20000, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 20000, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);
		try {
			holdSeatsDao.holdSeats(requestedSeats, minLevel,
					maxLevel, 1, vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when No Seats Available.
	 */
	@Test
	public void holdSeatsInvalidNoSeats() {
		int requestedSeats = 1000;
		Integer maxLevel = 2;
		Integer minLevel = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);

		try {
			holdSeatsDao.holdSeats(requestedSeats, minLevel,
					maxLevel, 1, vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when No Seats Available Between
	 * Range.
	 */
	@Test
	public void holdSeatsInvalidNoSeatsBetweenRange() {
		int requestedSeats = 21;
		Integer maxLevel = 2;
		Integer minLevel = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);

		try {
			holdSeatsDao.holdSeats(requestedSeats, minLevel,
					maxLevel, 1, vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when No Seats Available for Min
	 * Range.
	 */
	@Test
	public void holdSeatsInvalidNoSeatsMinRange() {
		int requestedSeats = 21;
		Integer minLevel = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);

		try {
			holdSeatsDao.holdSeats(requestedSeats, minLevel, null,
					1, vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when No Seats Available for Max
	 * Range.
	 */
	@Test
	public void holdSeatsInvalidNoSeatsMaxRange() {
		int requestedSeats = 21;
		Integer maxLevel = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);

		try {
			holdSeatsDao.holdSeats(requestedSeats, null, maxLevel,
					1, vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when No Seats Available without any
	 * Range.
	 */
	@Test
	public void holdSeatsInvalidNoSeatsNoRange() {
		int requestedSeats = 21;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(2, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);
		vanueLevelDetails.add(dto);

		try {
			holdSeatsDao.holdSeats(requestedSeats, null, null, 1,
					vanueLevelDetails, "");
			fail("Must throw Not Found Exception");
		} catch (NotFoundException e) {
			assertTrue(e.getError().getId().equals("200004"));
			assertTrue(e.getError().getClientText().equals(NOT_FOUND_ERROR));

		}

	}

	/**
	 * Test class for Hold Seats Error path when Exactly sameNo of Seats
	 * Available without any Range.
	 */
	@Test
	public void holdSeatsInvalidExactNoOfSeats() {
		int requestedSeats = 20;
		HoldSeatDTO holdSeatDTO;
		int vanueID = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(1, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);

		String customersEmail = "test";
		org.springframework.amqp.rabbit.connection.Connection conn = mock(Connection.class);
		ConnectionFactory connectionFactory = mock(ConnectionFactory.class);

		Map<String, Object> args = new HashMap<String, Object>();
		args.put("x-dead-letter-exchange", "");
		args.put("x-dead-letter-routing-key", DESTINATION_QUEUE);
		args.put("x-message-ttl", 120000);

		when(rabbitMqConfig.connectionFactory()).thenReturn(connectionFactory);
		when(connectionFactory.createConnection()).thenReturn(conn);
		when(conn.createChannel(false)).thenReturn(channel);
		holdSeatDTO = holdSeatsDao.holdSeats(requestedSeats, null, null,
				vanueID, vanueLevelDetails, customersEmail);
		assertNotNull(holdSeatDTO);
		assertNotNull(holdSeatDTO.getSeatHoldId());
		assertNotNull(holdSeatDTO.getNoOfSeats());
		assertTrue(holdSeatDTO.getCustomerEmail().equals(customersEmail));

		assertNotNull(holdSeatDTO.getSeatsDetails());
		assertTrue(holdSeatDTO.getSeatsDetails().size() > 0);
	}

	/**
	 * Test class for Hold Seats Error path when Exactly sameNo of Seats
	 * Available without any Range.
	 */
	@Test
	public void holdSeatsMQNotAvaiable() {
		int requestedSeats = 20;
		int vanueID = 1;
		List<VanueDetailsDTO> vanueLevelDetails = new ArrayList<VanueDetailsDTO>();
		VanueDetailsDTO dto = new VanueDetailsDTO(1, "Test", "$100", 10, 1);
		VanueDetailsDTO dto2 = new VanueDetailsDTO(1, "Test", "$100", 10, 1);

		vanueLevelDetails.add(dto);
		vanueLevelDetails.add(dto2);

		String customersEmail = "test";

		try {
			holdSeatsDao.holdSeats(requestedSeats, null, null, vanueID,
					vanueLevelDetails, customersEmail);
			fail("Must throw Not Found Exception");
		} catch (RuntimeException e) {
			assertTrue(e.getMessage().equals(SYSTEM_ERROR));

		}

	}
}
