package com.walmart.ticketservice.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

/**
 * This is the POJO object for Exception.
 * 
 * @author Maharshi
 *
 */
public class APIError {

	@Autowired
	private MessageSource messageSource;

	private String type;
	private String id;
	private String clientText;
	private String developerText;
	private String actions;

	public APIError(final String type, final String id,
			final String clientText, final String developerText,
			final String actions) {
		super();
		this.type = type;
		this.id = id;
		this.clientText = clientText;
		this.developerText = developerText;
		this.actions = actions;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientText() {
		return clientText;
	}

	public void setClientText(String clientText) {
		this.clientText = clientText;
	}

	public String getDeveloperText() {
		return developerText;
	}

	public void setDeveloperText(String developerText) {
		this.developerText = developerText;
	}

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

}
