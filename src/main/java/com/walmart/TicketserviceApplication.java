package com.walmart;

import javax.inject.Inject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;

import com.walmart.ticketservice.model.VanueDetailsDTO;
import com.walmart.ticketservice.mongo.repo.VanueDetailsMongoRepository;

/**
 * This is the Spring boot Application class for running the application.
 * 
 * @author maharshi
 *
 * @SpringBootApplication is a convenience annotation that adds all of the
 *                        following:
 * @Configuration tags the class as a source of bean definitions for the
 *                application context.
 * @EnableAutoConfiguration tells Spring Boot to start adding beans based on
 *                          classpath settings and various property settings.
 */
@SpringBootApplication(scanBasePackages = { "com.walmart",
		"com.walmart.ticketservice.rest.resources.v1" })
public class TicketserviceApplication extends SpringBootServletInitializer
		implements CommandLineRunner {

	@Inject
	private VanueDetailsMongoRepository vanueRepo;

	/*
	 * The main() method uses Spring Boot’s SpringApplication.run() method to
	 * launch an application.
	 */
	public static void main(String[] args) {

		SpringApplication.run(TicketserviceApplication.class, args);
	}

	/*
	 * The run() method returns an ApplicationContext and this application then
	 * retrieves all the beans that were created either by your app or were
	 * automatically added. Setting up the initial Load for Mongo DB.
	 */
	@Override
	public void run(String... args) throws Exception {

		vanueRepo.deleteAll();

		vanueRepo.save(new VanueDetailsDTO(1, "Orchestra", "$100", 25 * 50, 1));
		vanueRepo.save(new VanueDetailsDTO(2, "Main", "$75", 20 * 100, 1));
		vanueRepo.save(new VanueDetailsDTO(3, "Balcony 1", "$50", 15 * 100, 1));
		vanueRepo.save(new VanueDetailsDTO(4, "Balcony 2", "$40", 15 * 100, 1));

	}

	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		return builder.sources(TicketserviceApplication.class);
	}

}
