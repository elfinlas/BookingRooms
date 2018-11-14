package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.*;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.enums.MeetingTypeEnum;
import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.domain.pages.PageMaker;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.MeetingMember;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.jpa.persistence.MeetingAttendMemberRepo;
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
import java.util.regex.Pattern;
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
    private MeetingAttendMemberRepo attendMemberRepo;

    public MeetingDataService(ModelMapper modelMapper, MeetingRepoService meetingRepoService, AccountRepo accountRepo, MeetingAttendMemberRepo attendMemberRepo) {
        this.modelMapper = modelMapper;
        this.meetingRepoService = meetingRepoService;
        this.accountRepo = accountRepo;
        this.attendMemberRepo = attendMemberRepo;
    }


    /**
     * 회의 데이터를 페이징으로 가져오는 메서드
     * @param criteria
     * @return
     */
    public List<MeetingDTO> getMeetingAllData4Paging(Criteria criteria) {
        return meetingRepoService.getAllMeetingPageList(criteria).stream()
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * PageMaker를 가져오는 메서드
     * @param criteria
     * @return
     */
    public PageMaker getPageMaker(Criteria criteria) {
        return meetingRepoService.getPageMaker(criteria);
    }

    /**
     * 인덱스에 맞는 데이터를 가져오는 메서드
     * @param index
     * @return
     */
    public JsonResponseVO getMeetingData4Index(int index) {
        return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_GET_SUCCESS, modelMapper.map(meetingRepoService.getMeeting4Idx(index), MeetingDTO.class));
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
        List<MeetingDTO> meetingDTOList = meetingRepoService.getMeeting4Room(roomList, start, end).stream()
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
        RoomInMeetingDTO dto = new RoomInMeetingDTO()
                .setRoomList(roomDTOList)
                .setMeetingList(meetingDTOList);
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
        List<MeetingDTO> meetingDTOList = meetingRepoService.getMeeting4DuringStartEnd(startDate, endDate).stream()
                .map(meeting -> {
                    meeting.getAttendMemberList().stream().map(meetingMember -> {
                        MeetingMemberDTO dto = modelMapper.map(meetingMember, MeetingMemberDTO.class);
                        dto.setAttendCompanyMember(modelMapper.map(meetingMember.getAttendCompanyMember(), AccountDTO.class));
                        return dto;
                    });
                    return modelMapper.map(meeting, MeetingDTO.class);
                })
                .collect(Collectors.toList());
        return new JsonResponseVO(JsonResponseEnum.MEETING_CAL_DATA_GET_SUCCESS, meetingDTOList);
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
                .setPublic(!dto.getIsPublic())
                .setMeetingType(MeetingTypeEnum.NORMAL)
                .setCreateAccount(accountRepo.getOne(2)) //임시용 코드
                .setRoom(dto.getRoom());
        List<MeetingMember> attendMemberList = new ArrayList<>(); //참석인원을 담을 객체


        for (String user : dto.getAttendUserList()) { //순회
            MeetingMember member = new MeetingMember();
            if (Pattern.matches("^[0-9]*$", user)) { member.setAttendCompanyMember(accountRepo.findByAccountIdx(Integer.parseInt(user))); }//회사 인원
            else { member.setAttendOutMember(user); } //외부 추가 인원

            member.setMeeting(meeting); //회의 참석 인원 데이터
            attendMemberList.add(member);
        }
        meeting.setAttendMemberList(attendMemberList);
        meeting = meetingRepoService.saveData(meeting); //Save

        //회의 참석 인원 저장
        for (MeetingMember member: meeting.getAttendMemberList()) {
            attendMemberRepo.save(member);
        }

        return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_SUCCESS);
    }


    /**
     * 업데이트를 진행하는 메서드
     * @param dto
     * @return
     */
    public JsonResponseVO updateData(MeetingDTO dto) {
        Meeting meeting = meetingRepoService.getMeeting4Idx(dto.getMeetingIdx());
        meeting.setTitle(dto.getTitle())
                .setContent(dto.getContent())
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate())
                .setPublic(dto.getIsPublic())
                .setRoom(dto.getRoom());

        meetingRepoService.updateData(meeting); //업데이트 처리 진행

        List<String> outMemberList = new ArrayList<>(); //추가된 외부인을 담는 객체
        List<Account> companyMemberList = new ArrayList<>(); //추가된 임직원을 담는 객체

        for (String user : dto.getAttendUserList()) { //순회
            if (Pattern.matches("^[0-9]*$", user)) { companyMemberList.add(accountRepo.findByAccountIdx(Integer.parseInt(user))); }//회사 인원
            else { outMemberList.add(user); } //외부 추가 인원
        }

        //삭제 체크
        for (MeetingMember meetingMember: meeting.getAttendMemberList()) {
            if (meetingMember.getAttendOutMember() != null) { //외부 추가가 있는 경우
                if (!outMemberList.contains(meetingMember.getAttendOutMember())) { attendMemberRepo.delete(meetingMember); } //수정 시 삭제된 사용자는 삭제 처리를 진행
                else { outMemberList.remove(meetingMember.getAttendOutMember()); } //기존에 데이터는 배열에서만 삭제해준다.
            }
            else if (meetingMember.getAttendCompanyMember() != null) { //회사 사용자가 있는 경우
                if (!companyMemberList.contains(meetingMember.getAttendCompanyMember())) { attendMemberRepo.delete(meetingMember); } //수정 시 삭제된 사용자는 삭제 처리를 진행
                else { companyMemberList.remove(meetingMember.getAttendCompanyMember()); } //기존에 데이터는 배열에서만 삭제해준다.
            }
        }

        //추가적으로 등록된 외부 사용자 및 사내 사용자를 저장한다.
        outMemberList.forEach(user -> {
            MeetingMember member = new MeetingMember().setMeeting(meeting).setAttendOutMember(user);
            meeting.getAttendMemberList().add(member);
            attendMemberRepo.save(member);
        });
        companyMemberList.forEach(account -> {
            MeetingMember member = new MeetingMember().setMeeting(meeting).setAttendCompanyMember(account);
            meeting.getAttendMemberList().add(member);
            attendMemberRepo.save(member);
        });

        return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_UPDATE_SUCCESS, "Success");
    }


    /**
     * 회의 데이터를 삭제하는 메서드
     * @param meetingIdx
     * @return
     */
    public JsonResponseVO deleteMeetingData(int meetingIdx) {
        Meeting meeting = meetingRepoService.getMeeting4Idx(meetingIdx);
        //회의 참석 인원 데이터를 삭제해 준다.
        for (MeetingMember meetingMember: meeting.getAttendMemberList()) {
            attendMemberRepo.delete(meetingMember);
        }

        meetingRepoService.deleteData(meeting);
        return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_DELETE_SUCCESS);
    }



}
