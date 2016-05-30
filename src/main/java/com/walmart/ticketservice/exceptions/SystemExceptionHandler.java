package com.walmart.ticketservice.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
/**
 * This is the System exception class.
 * @author Maharshi
 *
 */
public class SystemExceptionHandler implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException arg0) {
		APIError error = new APIError("errorResponse", "200002", "System is not available currently.",arg0.getMessage()!=null?arg0.getMessage().toString():null,"Contact:maharshijha@hotmail.com");

        return Response.status(Status.SERVICE_UNAVAILABLE).entity(error).build();  
	}

}
