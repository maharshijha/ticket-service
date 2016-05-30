package com.walmart.ticketservice.model;

/**
 * This is the POJO for the Seat object.
 * 
 * @author MAharshi
 *
 */
public class Seat {

	private int noOfSeat;
	private int levelId;
	private String levenName;

	public Seat() {
	}

	public Seat(int noOfSeat, int levelId, String levenName) {
		super();
		this.noOfSeat = noOfSeat;
		this.levelId = levelId;
		this.levenName = levenName;
	}

	public int getNoOfSeat() {
		return noOfSeat;
	}

	public void setNoOfSeat(int noOfSeat) {
		this.noOfSeat = noOfSeat;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public String getLevenName() {
		return levenName;
	}

	public void setLevenName(String levenName) {
		this.levenName = levenName;
	}

}
