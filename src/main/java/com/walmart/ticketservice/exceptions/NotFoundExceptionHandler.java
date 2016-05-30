package com.walmart.ticketservice.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
/**
 * Exception intercepter for the exception.
 * @author Maharshi
 *
 */
public class NotFoundExceptionHandler implements
		ExceptionMapper<NotFoundException> {
	@Override
	public Response toResponse(NotFoundException exception) {
		return Response.status(Status.NOT_FOUND).entity(exception.getError())
				.build();
	}

}