package com.mhlab.br;

import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.jpa.persistence.RoomRepo;
import com.mhlab.br.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDateTime;


@SpringBootApplication
@EntityScan(basePackages = {"com.mhlab.br.jpa.entity"})
@EnableJpaRepositories(basePackages = "com.mhlab.br.jpa.persistence")
public class BookingRoomsApplication implements CommandLineRunner {

	@Autowired private AccountRepo accountRepo;
	@Autowired private RoomRepo roomRepo;

	public static void main(String[] args) {
		SpringApplication.run(BookingRoomsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		Room room = new Room()
//				.setName("Room One")
//				.setDescription("Test Room One");
//
//		roomRepo.save(room);


//		Account adminAccount = new Account()
//				.setId("test1")
//				.setName("testUser")
//				.setTeamName("system")
//				.setPw(SecurityUtils.encryptData4SHA("1234"))
//				.setCreateDate(LocalDateTime.now())
//				.setUpdateDate(LocalDateTime.now());
//		accountRepo.save(adminAccount);
	}
}
