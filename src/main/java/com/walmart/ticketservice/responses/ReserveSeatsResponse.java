package com.walmart.ticketservice.responses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Response Object for the /reserveSeats resource.
 * 
 * @author Maharshi
 *
 */
@ApiModel(value = "Response indicated Booking Reference ID for booked seats")
public class ReserveSeatsResponse {

	@ApiModelProperty(value = "Booking Refrence Code")
	private String bookingReferenceCode;

	public ReserveSeatsResponse(String bookingReferenceCode) {
		super();
		this.bookingReferenceCode = bookingReferenceCode;
	}

	public String getBookingReferenceCode() {
		return bookingReferenceCode;
	}

	public void setBookingReferenceCode(String bookingReferenceCode) {
		this.bookingReferenceCode = bookingReferenceCode;
	}

}
