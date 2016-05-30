package com.walmart;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

import javax.inject.Named;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.wadl.internal.WadlResource;
import org.springframework.context.annotation.Configuration;

import com.walmart.ticketservice.exceptions.NotFoundExceptionHandler;
import com.walmart.ticketservice.exceptions.SystemExceptionHandler;
import com.walmart.ticketservice.exceptions.ValidationExceptionHandler;
import com.walmart.ticketservice.rest.resources.v1.TicketServiceResource;
import com.walmart.ticketservice.secure.AuthenticationFilter;

/**
 * 
 * @author maharshi This Class will register Application resource class.
 *
 */
@Named
@Configuration
public class JerseyConfig extends ResourceConfig {

	private String apiPath = "/api";

	/**
	 * In constructor we can define Jersey Resources &amp; Other Components
	 */
	public JerseyConfig() {
		register(TicketServiceResource.class);
		register(NotFoundExceptionHandler.class);
		register(SystemExceptionHandler.class);
		register(ValidationExceptionHandler.class);
		register(AuthenticationFilter.class);
		property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
		property(
				ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK,
				true);
		this.register(WadlResource.class);
		this.configureSwagger();

	}

	private void configureSwagger() {
		// Available at localhost:port/swagger.json
		this.register(ApiListingResource.class);
		this.register(SwaggerSerializers.class);
		BeanConfig config = new BeanConfig();
		config.setConfigId("Ticket Booking Service UI");
		config.setTitle("Ticket Booking Service UI");
		config.setVersion("v1");
		config.setContact("Maharshi Jha");
		config.setSchemes(new String[] { "http", "https" });
		config.setBasePath(apiPath);
		config.setResourcePackage("com.walmart.ticketservice.rest.resources.v1");
		config.setPrettyPrint(true);
		config.setScan(true);

		String[] packages = { "com.walmart.ticketservice.rest.resources.v1" };

		packages(packages);
	}
}