package com.walmart.ticketservice.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * This is the ReserveSeats Request class for /reserveSeats resource.
 * 
 * @author Maharshi
 *
 */
@ApiModel(value = "Request indicated total number of seats avaliable for given vanue")
public class ReserveSeatsRequest {

	@Valid()
	@NotNull(message = "The seatHoldId must not be null")
	@Min(value = 1, message = "The seatHoldId must be greater then 0")
	@ApiModelProperty(value = "Seat Hold Id")
	private Integer seatHoldId;

	@Valid()
	@NotNull(message = "The customerEmail must not be null")
	@ApiModelProperty(value = "Customer's Email")
	private String customerEmail;

	public int getSeatHoldId() {
		return seatHoldId;
	}

	public void setSeatHoldId(int seatHoldId) {
		this.seatHoldId = seatHoldId;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

}
