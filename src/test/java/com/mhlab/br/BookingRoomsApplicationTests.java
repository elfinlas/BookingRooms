package com.mhlab.br;

import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.RoomRepo;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BookingRoomsApplicationTests {

	@Autowired private RoomRepo roomRepo;
	@Autowired private RoomRepoService roomRepoService;

	@Test
	public void contextLoads() {

		List<Room> list3 = roomRepo.findAllBy(PageRequest.of(0,3, Sort.Direction.DESC, "createDate"));
		for (Room room : list3) {
			log.info("room3 = " + room.getName());
		}

		log.info("======================");

		Criteria criteria = new Criteria();
		criteria.setPage(0);
		criteria.setPerPageNum(3);

		//log.info("criteria = " + criteria.toString());

		log.info("======================");

		List<Room> list5 = roomRepoService.getAllRoomPageList(criteria);
		for (Room room : list5) {
			log.info("room5 = " + room.getName());
		}

		log.info("======================");

	}

}
