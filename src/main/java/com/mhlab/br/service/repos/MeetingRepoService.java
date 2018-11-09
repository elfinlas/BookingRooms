package com.mhlab.br.service.repos;

import com.mhlab.br.domain.dto.AccountDTO;
import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.domain.dto.MeetingMemberDTO;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.MeetingMember;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.MeetingAttendMemberRepo;
import com.mhlab.br.jpa.persistence.MeetingRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;
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

    /**
     * 특정 회의실에서 진행한 회의 데이터를 가져오는 메서드
     * @param roomList
     * @return
     */
    public List<MeetingDTO> getMeeting4Room(List<Room> roomList) {
        return roomList.stream()
                .map(room -> meetingRepo.findByRoom(room))
                .flatMap(List::stream)
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 특정 회의실에서 진행 및 시작, 종료 시간에 맞는 데이터를 가져오는 메서드
     * @param roomList
     * @param start
     * @param end
     * @return
     */
    public List<MeetingDTO> getMeeting4Room(List<Room> roomList, LocalDateTime start, LocalDateTime end) {
        return roomList.stream()
                .map(room -> meetingRepo.findByRoomAndStartDateAfterAndEndDateBeforeOrderByStartDate(room, start, end))
                .flatMap(List::stream)
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 회의 시작 및 종료 시간에 맞는 데이터를 가져오는 메서드
     * @param start
     * @param end
     * @return
     */
    public List<MeetingDTO> getMeeting4DuringStartEnd(LocalDateTime start, LocalDateTime end) {
        return meetingRepo.findByStartDateAfterAndEndDateBeforeOrderByStartDate(start, end).stream()
                .map(meeting -> {
                    meeting.getAttendMemberList().stream().map(meetingMember -> {
                        MeetingMemberDTO dto = modelMapper.map(meetingMember, MeetingMemberDTO.class);
                        dto.setAttendCompanyMember(modelMapper.map(meetingMember.getAttendCompanyMember(), AccountDTO.class));
                        return dto;
                    });
                    return modelMapper.map(meeting, MeetingDTO.class);
                })
                .collect(Collectors.toList());
    }

    /**
     * 데이터를 저장하는 메서드
     * @param data
     */
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


    /**
     * 회의 데이터를 삭제하는 메서드
     * @param idx
     */
    public void deleteData(int idx) {
        deleteData(meetingRepo.getOne(idx));
    }


    /**
     * 회의 데이터를 삭제하는 메서드
     * @param data
     */
    public void deleteData(Meeting data) {
        //회의 참석 인원 데이터를 삭제해 준다.
        for (MeetingMember meetingMember: data.getAttendMemberList()) {
            attendMemberRepo.delete(meetingMember);
        }

        //회의 데이터 삭제
        meetingRepo.delete(data);
    }
}
