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

		/*
		2 - Room 2F B
		3 - B-Tower 19 A
		4 - Test Room1
		1 - Room A
		 */


//		List<Room> list1 = roomRepo.findAllBy(PageRequest.of(1,2));
//		for (Room room : list1) {
//			log.info("room3 = " + room.getName());
//		}
//
//		List<Room> list2 = roomRepo.findAllBy(PageRequest.of(0,3));
//		for (Room room : list2) {
//			log.info("room4 = " + room.getName());
//		}

		List<Room> list3 = roomRepo.findAllBy(PageRequest.of(0,3, Sort.Direction.DESC, "createDate"));
		for (Room room : list3) {
			log.info("room3 = " + room.getName());
		}

		List<Room> list4 = roomRepo.findAllBy(PageRequest.of(1,3, Sort.Direction.DESC, "createDate"));
		for (Room room : list4) {
			log.info("room4 = " + room.getName());
		}

		Criteria criteria = new Criteria();
		criteria.setPage(0);
		criteria.setPerPageNum(2);

		List<Room> list5 = roomRepoService.getAllRoomPageList(criteria);
		for (Room room : list5) {
			log.info("room5 = " + room.getName());
		}

		/*
		room3 = B-Tower 19 A - 3
		room3 = Test Room1 - 4

		room4 = Room A 1 -
		room4 = Room 2F B 2 -
		 */


	}

}
