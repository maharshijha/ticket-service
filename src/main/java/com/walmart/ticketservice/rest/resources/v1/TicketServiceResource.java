package com.walmart.ticketservice.rest.resources.v1;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.JSONP;

import com.walmart.ticketservice.model.HoldSeatDTO;
import com.walmart.ticketservice.requests.FindAndHoldSeatsRequest;
import com.walmart.ticketservice.requests.ReserveSeatsRequest;
import com.walmart.ticketservice.responses.ReserveSeatsResponse;
import com.walmart.ticketservice.responses.SeatsAvailableResponse;
import com.walmart.ticketservice.service.TicketService;
/**
 * Resource Class for Ticket Service.
 * @author Maharshi
 *
 */

@Named
@Path("/walmart/onlineServices/vanue/{vanueId}/v1")
@Api(value = "Booking Service Resources", produces=MediaType.APPLICATION_JSON)
public class TicketServiceResource {

	@Inject
	TicketService service;
	
	private static final String VANUE_NOT_NULL="VanueId must not be null";
	
	private static final String VANUE_NOT_VALID="VanueId must be greater then 0";
	
	private static final String LEVEL_NOT_VALID="LevelId must be greater then 0";
	
	private static final String HEADERNAME="api-key";

	private static final String HEADERVALUE="API-KEY";

	private static final String HEADERDEFAULTVALUE="WALMART";


	/**
	 * Get Call for Find the number of seats available within the venue.
	 * @param vanueId
	 * @param levelId
	 * @return SeatsAvailableResponse
	 */
	@GET
	@Path("/availableSeats")
	@JSONP(queryParam = JSONP.DEFAULT_QUERY)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find the number of seats available within the venue, optionally by seating level", notes = "Note: available seats are seats that are neither held nor reserved.")
	@ApiResponses({
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Found Available Seats.",response=SeatsAvailableResponse.class),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "No Resource Found for given details."),
			@ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN, message = "Access Denied."),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE, message = "Service Not Availabel"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Request")})
	@ApiImplicitParams({
	    @ApiImplicitParam(name = HEADERNAME, value = HEADERVALUE, required = true, dataType = "string", paramType = "header", defaultValue=HEADERDEFAULTVALUE)	    
	  })
	public Response numSeatsAvailable(@ApiParam(value = "VanueID for future expansion,Currenntly default is 1",defaultValue="1") @Valid @NotNull(message=VANUE_NOT_NULL) @PathParam("vanueId") @Min(value=1,message=VANUE_NOT_VALID)Integer vanueId,@ApiParam(value = "Level ID for find the number of seats by seating level")@Valid @Min(value=1,message=LEVEL_NOT_VALID)@QueryParam("levelId") Integer levelId) {

		SeatsAvailableResponse seatsAvailableResponse=new SeatsAvailableResponse(service.numSeatsAvailable(levelId));
				
		return Response.status(HttpURLConnection.HTTP_OK).entity(seatsAvailableResponse).build();
	}
	
	/**
	 * Post Call for holding the requested seats.
	 * @param request
	 * @param vanueId
	 * @return HoldSeat.class
	 */
	@POST
	@Path("/holdSeats")
	@JSONP(queryParam = JSONP.DEFAULT_QUERY)
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find and hold the best available seats on behalf of a customer.", notes = "Note: each ticket hold will expire within 2 mins.")
	@ApiResponses({
			@ApiResponse(code = HttpURLConnection.HTTP_CREATED, message = "Created",response=HoldSeatDTO.class),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "No Resource Found for given details."),
			@ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN, message = "Access Denied."),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE, message = "Service Not Availabel"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Request")})
	@ApiImplicitParams({
	    @ApiImplicitParam(name = HEADERNAME, value = HEADERVALUE, required = true, dataType = "string", paramType = "header", defaultValue=HEADERDEFAULTVALUE)	    
	  })
	public Response findAndHoldSeats(@Valid FindAndHoldSeatsRequest request,@ApiParam(value = "VanueID for future expansion,Currenntly default is 1",defaultValue="1")@Valid @NotNull(message=VANUE_NOT_NULL) @PathParam("vanueId") @Min(value=1,message=VANUE_NOT_VALID) String vanueId) {
	
		HoldSeatDTO dto=service.findAndHoldSeats(request.getNumSeats(), request.getMinLevel(), request.getMaxLevel(), request.getCustomerEmail());
		return Response.status(HttpURLConnection.HTTP_CREATED).entity(dto).build();
	}
	
	/**
	 * PUT Call for reserving the held Ticket.
	 * @param request
	 * @param vanueId
	 * @return String
	 */
	@PUT
	@Path("/reserveSeats")
	@JSONP(queryParam = JSONP.DEFAULT_QUERY)
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Reserve and commit a specific group of held seats for a customer")
	@ApiResponses(value={
			@ApiResponse(code = HttpURLConnection.HTTP_OK, message  = "Ok"),
			@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "No Resource Found for given details."),
			@ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN, message = "Access Denied."),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE, message = "Service Not Availabel"),
			@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Invalid Request")})
	@ApiImplicitParams({
	    @ApiImplicitParam(name = HEADERNAME, value = HEADERVALUE, required = true, dataType = "string", paramType = "header", defaultValue=HEADERDEFAULTVALUE)	    
	  })
	public Response reserveSeats(@Valid ReserveSeatsRequest request,@ApiParam(value = "VanueID for future expansion,Currenntly default is 1",defaultValue="1")@Valid @NotNull(message=VANUE_NOT_NULL) @PathParam("vanueId") @Min(value=1,message=VANUE_NOT_VALID) String vanueId){
	
		ReserveSeatsResponse reserveSeatsResponse=new ReserveSeatsResponse(service.reserveSeats(request.getSeatHoldId(), request.getCustomerEmail()));
		return Response.status(HttpURLConnection.HTTP_OK).entity(reserveSeatsResponse).build();
	}
	
	/**
	 * Delete call for Reseting Service/Mongo DB to intial load.
	 * @return
	 */
	@DELETE
	@Path("/resetService")
	@JSONP(queryParam = JSONP.DEFAULT_QUERY)
	@Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Reseting Service/Mongo DB to intial load", response = Response.class)
	@ApiResponses(value={
			@ApiResponse(code = HttpURLConnection.HTTP_NO_CONTENT, message  = "Success"),
			@ApiResponse(code = HttpURLConnection.HTTP_FORBIDDEN, message = "Access Denied."),
			@ApiResponse(code = HttpURLConnection.HTTP_UNAVAILABLE, message = "Service Not Availabel")})
	@ApiImplicitParams({
	    @ApiImplicitParam(name = HEADERNAME, value = HEADERVALUE, required = true, dataType = "string", paramType = "header", defaultValue=HEADERDEFAULTVALUE)	    
	  })
	public Response resetService(){
		service.resetService();
		return Response.status(HttpURLConnection.HTTP_NO_CONTENT).build();
	}
	
}
