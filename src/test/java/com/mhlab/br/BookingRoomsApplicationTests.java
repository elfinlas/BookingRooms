package com.mhlab.br;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.jpa.persistence.MeetingRepo;
import com.mhlab.br.jpa.persistence.RoomRepo;
import com.mhlab.br.service.data.MeetingDataService;
import com.mhlab.br.service.repos.MeetingRepoService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BookingRoomsApplicationTests {

	@Autowired private AccountRepo accountRepo;
	@Autowired private RoomRepo roomRepo;
	@Autowired private MeetingRepo meetingRepo;

	@Autowired private MeetingRepoService meetingRepoService;
	@Autowired private MeetingDataService meetingDataService;

	@Test
	@Transactional
	public void contextLoads() {
		for (MeetingDTO dto : meetingDataService.getMeetingData4Week()) {
			log.info("dto = " + dto.getTitle());
			log.info("member = " + dto.getAttendMemberList() );
			log.info("mem = " + dto.getAttendUserList());
		}
	}

}
