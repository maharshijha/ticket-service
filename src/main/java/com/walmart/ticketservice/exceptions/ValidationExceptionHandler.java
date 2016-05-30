package com.walmart.ticketservice.exceptions;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * This is the request validation exception.
 * @author Maharshi
 *
 */
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException>{

	@Override
	public Response toResponse(ConstraintViolationException arg0) {
		APIError error = new APIError("errorResponse", "200002", "Request is not valid please check.",arg0.getConstraintViolations().toString(),"Contact:maharshijha@hotmail.com");
		
        return Response.status(Status.SERVICE_UNAVAILABLE).entity(error).build();  

	}

}
