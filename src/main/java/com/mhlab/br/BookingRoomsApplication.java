package com.mhlab.br;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EntityScan(basePackages = {"com.mhlab.br.jpa.entity"})
@EnableJpaRepositories(basePackages = "com.mhlab.br.jpa.persistence")
public class BookingRoomsApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(BookingRoomsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
