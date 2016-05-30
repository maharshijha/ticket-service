package com.walmart.ticketservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * This is the DTO object for the vanue details.
 * 
 * @author Maharshi
 *
 */
@Document(collection = "vanuedetails")
public class VanueDetailsDTO {

	public VanueDetailsDTO(int levelId, String levelName, String price,
			int totalAvailableSeats, int vanueId) {
		super();
		this.levelId = levelId;
		this.levelName = levelName;
		this.price = price;
		this.totalAvailableSeats = totalAvailableSeats;
	}

	@Id
	private int levelId;
	@Indexed
	private int vanueId;

	private String levelName;
	private String price;
	private int totalAvailableSeats;

	public int getVanueId() {
		return vanueId;
	}

	public void setVanueId(int vanueId) {
		this.vanueId = vanueId;
	}

	public int getLevelId() {
		return levelId;
	}

	public void setLevelId(int levelId) {
		this.levelId = levelId;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getTotalAvailableSeats() {
		return totalAvailableSeats;
	}

	public void setTotalAvailableSeats(int totalAvailableSeats) {
		this.totalAvailableSeats = totalAvailableSeats;
	}

}
