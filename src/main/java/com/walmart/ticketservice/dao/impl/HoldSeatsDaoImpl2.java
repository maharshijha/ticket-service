package com.walmart.ticketservice.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.rabbitmq.client.Channel;
import com.walmart.RabbitMqConfig;
import com.walmart.ticketservice.dao.HoldSeatsDao;
import com.walmart.ticketservice.exceptions.NotFoundException;
import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.model.Seat;
import com.walmart.ticketservice.model.VanueDetailsDTO;
import com.walmart.ticketservice.model.HoldSeatDTO.SeatStatus;

public class HoldSeatsDaoImpl2 implements HoldSeatsDao{

//	@Override
//	public HoldSeatDTO holdSeats(int requestedSeats, Integer minLevel,
//			Integer maxLevel, int vanueID,
//			List<VanueDetailsDTO> vanueLevelDetails, String customersEmail) {
////		HoldSeatDTO dto=new HoldSeatDTO(111, 1111, "HoldSeat2", null, 1, 100000, "Algo2");
//		
//		
//		
//		
//		return dto;
//	}
	


	@Inject
	MongoTemplate mongoTemplet;

	@Inject
	RabbitMqConfig rabbitMqConfig;

	private final static String DELAYED_QUEUE = "delayed";

	private final static String DESTINATION_QUEUE = "direct";

	private final static String NOT_FOUND_ERROR = "No More Seats aviable currently";

	private final static String SYSTEM_ERROR = "MQ System is not avaiable currently";

	/**
	 * This Method will receive parameter from user and hold the seats
	 * accordingly.
	 * 
	 * @param numOfSeats
	 *            User requested Number of Seats
	 * @param minLevel
	 *            User requested minLevel
	 * @param maxLevel
	 *            User requested maxLevel
	 * @param vanueID
	 *            VanueID for future use.
	 * @param vanueLevelDetails
	 *            VanueDetailsDTO to identify umber of seats available and
	 *            update it.
	 * @param customersEmail
	 *            User Passed email ID
	 * @return
	 */
	public HoldSeatDTO holdSeats(final int requestedSeats,
			final Integer minLevel, final Integer maxLevel, final int vanueID,
			final List<VanueDetailsDTO> vanueLevelDetails,
			final String customersEmail) {

		VanueDetailsDTO VanueDetailsDTO;
		int maxNumSeats = requestedSeats;
		int numOfSeats = requestedSeats;
		int holdId;
		HoldSeatDTO holdSeatDTO;
		Seat seat;
		List<Seat> seats = new ArrayList<Seat>();
		List<VanueDetailsDTO> updatedVanueDetailsDTO = new ArrayList<VanueDetailsDTO>();

		Iterator<VanueDetailsDTO> VanueDetailsDTOList = vanueLevelDetails
				.iterator();

		/*
		 * Iterate over the each vanue. Identify the min and max level vanue
		 * level. Check if the requested seats are available or not. if
		 * requested seats are more then available then go for the next level.
		 * update the vanue available seat after holding the seats.
		 */
		while (VanueDetailsDTOList.hasNext() && numOfSeats > 0) {
			VanueDetailsDTO = VanueDetailsDTOList.next();
			if(getConsecutiveSeats(VanueDetailsDTO.getSeatIds(), VanueDetailsDTO.getColumn(),, noOfSeats))
			if (VanueDetailsDTO.getTotalAvailableSeats() > 0 && VanueDetailsDTO.getSeatIds().size() > requestedSeats) {
				if (null != minLevel && null != maxLevel
						&& VanueDetailsDTO.getLevelId() >= minLevel
						&& VanueDetailsDTO.getLevelId() <= maxLevel) {
					seat = new Seat(
							numOfSeats > VanueDetailsDTO.getTotalAvailableSeats() ? VanueDetailsDTO.getTotalAvailableSeats()
									: numOfSeats, VanueDetailsDTO.getLevelId(),
							VanueDetailsDTO.getLevelName());
					seats.add(seat);
					numOfSeats -= VanueDetailsDTO.getTotalAvailableSeats();
					VanueDetailsDTO.setTotalAvailableSeats(VanueDetailsDTO
							.getTotalAvailableSeats() - seat.getNoOfSeat());
					updatedVanueDetailsDTO.add(VanueDetailsDTO);
				} else if (null == minLevel && null != maxLevel
						&& VanueDetailsDTO.getLevelId() <= maxLevel) {
					seat = new Seat(
							numOfSeats > VanueDetailsDTO.getTotalAvailableSeats() ? VanueDetailsDTO.getTotalAvailableSeats()
									: numOfSeats, VanueDetailsDTO.getLevelId(),
							VanueDetailsDTO.getLevelName());
					seats.add(seat);
					numOfSeats -= VanueDetailsDTO.getTotalAvailableSeats();
					VanueDetailsDTO.setTotalAvailableSeats(VanueDetailsDTO
							.getTotalAvailableSeats() - seat.getNoOfSeat());
					updatedVanueDetailsDTO.add(VanueDetailsDTO);

				} else if (null == maxLevel && null != minLevel
						&& VanueDetailsDTO.getLevelId() >= minLevel) {
					seat = new Seat(
							numOfSeats > VanueDetailsDTO.getTotalAvailableSeats() ? VanueDetailsDTO.getTotalAvailableSeats()
									: numOfSeats, VanueDetailsDTO.getLevelId(),
							VanueDetailsDTO.getLevelName());
					seats.add(seat);
					numOfSeats -= VanueDetailsDTO.getTotalAvailableSeats();
					VanueDetailsDTO.setTotalAvailableSeats(VanueDetailsDTO
							.getTotalAvailableSeats() - seat.getNoOfSeat());
					updatedVanueDetailsDTO.add(VanueDetailsDTO);

				} else if (null == maxLevel && null == minLevel) {
					seat = new Seat(
							numOfSeats > VanueDetailsDTO.getTotalAvailableSeats() ? VanueDetailsDTO.getTotalAvailableSeats()
									: numOfSeats, VanueDetailsDTO.getLevelId(),
							VanueDetailsDTO.getLevelName());
					seats.add(seat);
					numOfSeats -= VanueDetailsDTO.getTotalAvailableSeats();
					VanueDetailsDTO.setTotalAvailableSeats(VanueDetailsDTO
							.getTotalAvailableSeats() - seat.getNoOfSeat());
					updatedVanueDetailsDTO.add(VanueDetailsDTO);

				}

			}

		}
		/*
		 * Generate the random booking ref number. Send the message to queue
		 * with holded seat object which will expire in 2 mins.
		 */
		if (numOfSeats <= 0 && requestedSeats > 0) {
			holdId = (int) (Math.random() * 1000000);
			holdSeatDTO = new HoldSeatDTO(0, holdId, customersEmail, seats,
					vanueID, maxNumSeats, SeatStatus.HOLD.toString());
			Connection conn=null;
			try {
				conn = rabbitMqConfig.connectionFactory()
						.createConnection();
				Channel channel = conn.createChannel(false);

				Map<String, Object> args = new HashMap<String, Object>();
				args.put("x-dead-letter-exchange", "");
				args.put("x-dead-letter-routing-key", DESTINATION_QUEUE);
				args.put("x-message-ttl", 120000);

				channel.queueDeclare(DELAYED_QUEUE, true, false, true, args);

				ObjectWriter ow = new ObjectMapper().writer()
						.withDefaultPrettyPrinter();
				String json = ow.writeValueAsString(holdSeatDTO);
				channel.basicPublish("", DELAYED_QUEUE, null, json.getBytes());
				conn.close();
				mongoTemplet.save(holdSeatDTO);
				for (int i = 0; i < updatedVanueDetailsDTO.size(); i++) {
					mongoTemplet.save(updatedVanueDetailsDTO.get(i));
				}
				return holdSeatDTO;
			} catch (Exception e) {
				if(null != conn){
					conn.close();
				}
				throw new RuntimeException(SYSTEM_ERROR, e);
			}
			
		} else {
			throw new NotFoundException("200004", NOT_FOUND_ERROR);

		}
	}

	
	
	public List getConsecutiveSeats(List availableSeats,int row,int column,int noOfSeats){
		
		List consSeats=new ArrayList();
		for(int i=0;i<availableSeats.size();i++){
			consSeats.clear();
			for(int j=0,k=i;j<noOfSeats;j++,k++){
				if(availableSeats.contains(availableSeats.get(i).toString().charAt(0)+(Integer.parseInt(availableSeats.get(i).toString().substring(1))+j))){
					consSeats.add(availableSeats.get(i));
				}
				else{
					break;
				}
					
			}
		}
		
		if(consSeats.size() == noOfSeats){
			return consSeats;
		}
		else{
			return null;
		}
	}


}
