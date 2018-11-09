package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.domain.dto.RoomDTO;
import com.mhlab.br.domain.dto.RoomInMeetingDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.enums.MeetingTypeEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.MeetingMember;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.service.repos.MeetingRepoService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 회의록 데이터를 처리하는 서비스 객체
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class MeetingDataService {

    private ModelMapper modelMapper;
    private MeetingRepoService meetingRepoService;
    private AccountRepo accountRepo;

    public MeetingDataService(ModelMapper modelMapper, MeetingRepoService meetingRepoService, AccountRepo accountRepo) {
        this.modelMapper = modelMapper;
        this.meetingRepoService = meetingRepoService;
        this.accountRepo = accountRepo;
    }


    /**
     * 회의실 별 회의 데이터를 가져오는 메서드
     * @param date
     * @param roomList
     * @return
     */
    public JsonResponseVO getMeetingData4Room(LocalDate date, List<Room> roomList) {
        //조회 시작, 종료 시간을 가져온다.
        LocalDateTime start = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 0,0,0);
        LocalDateTime end = LocalDateTime.of(date.getYear(), date.getMonth(), date.getDayOfMonth(), 23,59,59);
        List<RoomDTO> roomDTOList = roomList.stream()
                .map(room -> modelMapper.map(room, RoomDTO.class))
                .collect(Collectors.toList());
        RoomInMeetingDTO dto = new RoomInMeetingDTO()
                .setRoomList(roomDTOList)
                .setMeetingList(meetingRepoService.getMeeting4Room(roomList, start, end));
        return new JsonResponseVO(JsonResponseEnum.ROOM_MEETING_DATA_GET_SUCCESS, dto);
    }

    /**
     * 회의 데이터를 일정별로 가져오는 메서드
     * @param startStr
     * @param endStr
     * @return
     */
    public JsonResponseVO getMeetingData4Calendar(String startStr, String endStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime startDate = LocalDateTime.parse(startStr, formatter);
        LocalDateTime endDate = LocalDateTime.parse(endStr, formatter);
        return new JsonResponseVO(JsonResponseEnum.MEETING_CAL_DATA_GET_SUCCESS, meetingRepoService.getMeeting4DuringStartEnd(startDate, endDate));
    }


    /**
     * 회의 데이터 저장 메서드
     * @param dto
     * @return
     */
    public JsonResponseVO saveData(MeetingDTO dto) {
        Meeting meeting = new Meeting()
                .setTitle(dto.getTitle())
                .setContent(dto.getContent())
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate())
                .setPublic(dto.getIsPublic())
                //.setPublic(dto.isPublic())
                .setMeetingType(MeetingTypeEnum.NORMAL)
                .setCreateAccount(accountRepo.getOne(2)) //임시용 코드
                .setRoom(dto.getRoom());
        List<MeetingMember> attendMemberList = new ArrayList<>(); //참석인원을 담을 객체

        for (String user: dto.getAttendUserList()) {
            MeetingMember member = new MeetingMember();

            if(user.contains("-")) { member.setAttendOutMember(user.split("-")[0].trim()); }//추가된 사용자
            else { //기존 사용자
                Account target = accountRepo.findByAccountIdx(Integer.parseInt(user));
                member.setAttendCompanyMember(target);
            }

            member.setMeeting(meeting); //회의 참석 인원 데이터
            attendMemberList.add(member);
        }
        meeting.setAttendMemberList(attendMemberList);
        meetingRepoService.saveData(meeting);
        return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_SUCCESS);
    }


    /**
     * 회의 데이터를 삭제하는 메서드
     * @param meetingIdx
     * @return
     */
    public JsonResponseVO deleteMeetingData(int meetingIdx) {
        meetingRepoService.deleteData(meetingIdx);
        return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_DELETE_SUCCESS);
    }



}
