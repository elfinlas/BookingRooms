package com.mhlab.br;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookingRoomsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookingRoomsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
