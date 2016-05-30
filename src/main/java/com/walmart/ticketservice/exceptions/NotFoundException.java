package com.walmart.ticketservice.exceptions;

import java.io.Serializable;

/**
 * This is not found exception class.
 * 
 * @author Maharshi
 *
 */
public class NotFoundException extends RuntimeException implements Serializable {

	private APIError error;

	private static final long serialVersionUID = 1L;

	private static final String developerTest = "Requested Resource not avaiable";

	public NotFoundException(String errorCode, String errorText) {
		setError(new APIError("errorResponse", errorCode, errorText,
				developerTest, "Contact:maharshijha@hotmail.com"));
	}

	public APIError getError() {
		return error;
	}

	public void setError(APIError error) {
		this.error = error;
	}

}
