package com.walmart.ticketservice.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Service request class for holdSeats resource.
 * 
 * @author Maharshi
 *
 */

@ApiModel(value = "Request for finding and holding the requested seats.")
public class FindAndHoldSeatsRequest {

	@Valid()
	@NotNull(message = "The numSeats must not be null")
	@Min(value = 1, message = "The numSeats must be greater then 0")
	@ApiModelProperty(value = "Total Number of seats to hold")
	private Integer numSeats;

	@Min(value = 1, message = "The Minimum level be greater then 0")
	@ApiModelProperty(value = "Minimum level for holding seats", required = false)
	private Integer minLevel;

	@Min(value = 1, message = "The Maximum level must be greater then 0")
	@ApiModelProperty(value = "Maximum level for holding seats", required = false)
	private Integer maxLevel;

	@ApiModelProperty(value = "Custmer's Email")
	@NotNull(message = "The customerEmail must not be null")
	@Valid
	private String customerEmail;

	public Integer getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(Integer numSeats) {
		this.numSeats = numSeats;
	}

	public Integer getMinLevel() {
		return minLevel;
	}

	public void setMinLevel(Integer minLevel) {
		this.minLevel = minLevel;
	}

	public Integer getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(Integer maxLevel) {
		this.maxLevel = maxLevel;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

}
