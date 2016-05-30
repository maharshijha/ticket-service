package com.walmart.ticketservice.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This is the mongo collection model class for dtoring hold seat document in
 * mongo. This is the response class for holdSeats resource.
 * 
 * @author Maharshi
 *
 */
@Document(collection = "holdSeats")
@ApiModel(value = "Response indicated for held seats.")
public class HoldSeatDTO {

	public enum SeatStatus {

		HOLD {
			public String toString() {
				return "HOLD";
			}
		},

		BOOKED {
			public String toString() {
				return "BOOKED";
			}
		}
	}

	public HoldSeatDTO() {
	}

	public HoldSeatDTO(int bookinfRefNo, int seatHoldId, String customerEmail,
			List<Seat> seatsDetails, int vanueId, int noOfSeats, String status) {
		super();
		this.bookinfRefNo = bookinfRefNo;
		this.seatHoldId = seatHoldId;
		this.customerEmail = customerEmail;
		this.seatsDetails = seatsDetails;
		this.vanueId = vanueId;
		this.noOfSeats = noOfSeats;
		this.status = status;
	}

	private int bookinfRefNo;
	@ApiModelProperty(value = "Seat Hold ID")
	@Id
	private int seatHoldId;
	@ApiModelProperty(value = "Customer's Email")
	private String customerEmail;
	@ApiModelProperty(value = "Held Seat Details")
	private List<Seat> seatsDetails;
	@ApiModelProperty(value = "Vanue ID")
	private int vanueId;
	@ApiModelProperty(value = "No of Seats")
	private int noOfSeats;
	@ApiModelProperty(value = "Status of Seats")
	private String status;

	public List<Seat> getSeatsDetails() {
		return seatsDetails;
	}

	public void setSeatsDetails(List<Seat> seatsDetails) {
		this.seatsDetails = seatsDetails;
	}

	public int getVanueId() {
		return vanueId;
	}

	public void setVanueId(int vanueId) {
		this.vanueId = vanueId;
	}

	public int getBookinfRefNo() {
		return bookinfRefNo;
	}

	public void setBookinfRefNo(int bookinfRefNo) {
		this.bookinfRefNo = bookinfRefNo;
	}

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

	public int getVanue() {
		return vanueId;
	}

	public void setVanue(int vanueId) {
		this.vanueId = vanueId;
	}

	public int getNoOfSeats() {
		return noOfSeats;
	}

	public void setNoOfSeats(int noOfSeats) {
		this.noOfSeats = noOfSeats;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
