package com.mhlab.br.service.repos;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.MeetingMember;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.MeetingAttendMemberRepo;
import com.mhlab.br.jpa.persistence.MeetingRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 회의록 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class MeetingRepoService {

    private ModelMapper modelMapper;
    private MeetingRepo meetingRepo;
    private MeetingAttendMemberRepo attendMemberRepo;


    @Autowired
    public MeetingRepoService(ModelMapper modelMapper, MeetingRepo meetingRepo, MeetingAttendMemberRepo attendMemberRepo) {
        this.modelMapper = modelMapper;
        this.meetingRepo = meetingRepo;
        this.attendMemberRepo = attendMemberRepo;
    }


    //////////////////////////////
    //          Get
    //////////////////////////////

    //.stream().map(account -> modelMapper.map(account, AccountDTO.class))

    /////
    public List<MeetingDTO> getMeeting4Room(List<Room> roomList) {
        return roomList.stream()
                .map(room -> meetingRepo.findByRoom(room))
                .flatMap(List::stream)
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
        //return roomList.stream().map(room -> meetingRepo.findByRoom(room)).flatMap(List::stream).collect(Collectors.toList());
    }

    public List<MeetingDTO> getMeeting4Room(List<Room> roomList, LocalDateTime start, LocalDateTime end) {
        return roomList.stream()
                .map(room -> meetingRepo.findByRoomAndStartDateAfterAndEndDateBeforeOrderByStartDate(room, start, end))
                .flatMap(List::stream)
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
        //return roomList.stream().map(room -> meetingRepo.findByRoom(room)).flatMap(List::stream).collect(Collectors.toList());
    }


    //////

    public void saveData(Meeting data) {
        LocalDateTime now = LocalDateTime.now();
        data.setCreateDate(now);
        data.setUpdateDate(now);
        meetingRepo.save(data);

        //회의 참석 인원 저장
        for (MeetingMember member: data.getAttendMemberList()) {
            attendMemberRepo.save(member);
        }
    }

}
