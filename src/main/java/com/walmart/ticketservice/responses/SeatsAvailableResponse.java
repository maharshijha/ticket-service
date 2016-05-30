package com.walmart.ticketservice.responses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * This is the response class for available seat response.
 * @author Maharshi
 *
 */
@ApiModel(value = "Response indicated total number of seats avaliable for given vanue")
public class SeatsAvailableResponse {
	
	@ApiModelProperty(value = "Total Number of available seats")
	private int totalAvailableSeats;

	public SeatsAvailableResponse(int totalAvailableSeats) {
		super();
		this.totalAvailableSeats = totalAvailableSeats;
	}

	public int getTotalAvailableSeats() {
		return totalAvailableSeats;
	}

	public void setTotalAvailableSeats(int totalAvailableSeats) {
		this.totalAvailableSeats = totalAvailableSeats;
	}
	
	

}
