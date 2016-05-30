package com.walmart.ticketservice.secure;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.walmart.ticketservice.exceptions.APIError;

/**
 * This is the authentication filter which will check API-KEY
 * @author Maharshi
 *
 */
public class AuthenticationFilter implements javax.ws.rs.container.ContainerRequestFilter{

    private static final String AUTHORIZATION_PROPERTY = "API-KEY";

    private static final String AUTHORIZATION_EXCEPTION_SWAGGER = "swagger.json";
    
    private static final String ACCESS_DENIED ="You Don't have access to this resource";
   
    @Context
    UriInfo uriInfo;

    /**
     * This method will fetch headers from the context and check for the API-KEY.
     */
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		//Get request headers
        final MultivaluedMap<String, String> headers = requestContext.getHeaders();
          
        //Fetch authorization header
        final List<String> authorization = headers.get(AUTHORIZATION_PROPERTY);
        //If no authorization information present; block access
        //Always Allow swagger.json.
        if(!uriInfo.getPath().equals(AUTHORIZATION_EXCEPTION_SWAGGER)){
        	APIError error;
        if(authorization == null || authorization.isEmpty())
        {	error=new APIError("errorResponse", "200099", ACCESS_DENIED,ACCESS_DENIED,"Contact:maharshijha@hotmail.com");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(error).build());
            return;
        }
        
        //Get API-KEY
        if(!authorization.get(0).equals("WALMART")){
            error=new APIError("errorResponse", "200099", ACCESS_DENIED,ACCESS_DENIED,"Contact:maharshijha@hotmail.com");
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity(error).build());

        }
        }
        
	}

}
