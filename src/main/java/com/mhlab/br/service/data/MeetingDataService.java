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

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
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

    //시간 체크 사용 변수
    private static final int START_END_PASS = 0; //이상없음
    private static final int START_SAME = 1; //시작 시간 동일
    private static final int END_SAME = 2; //종료 시간 동일
    private static final int START_DURING = 3; //시작 시간이 겹침
    private static final int END_DURING = 4; //종료 시간이 겹침
    private static final int START_END_INCLUDE = 5; //시작 종료 시간이 포함됨

    //
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

    //////////////////////////
    //          Get
    //////////////////////////

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
        List<MeetingDTO> meetingDTOList = meetingRepoService.getMeeting4RoomListWithTime(roomList, start, end).stream()
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
        return new JsonResponseVO(JsonResponseEnum.MEETING_CAL_DATA_GET_SUCCESS, getMeetingData4Date(startDate, endDate));
    }

    /**
     * 오늘 회의 데이터를 가져오는 메서드
     * @return
     */
    public List<MeetingDTO> getMeetingData4Today() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 0,0,0);
        LocalDateTime endDate = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), 23,59,59);
        return getMeetingData4DateWithMemberStr(startDate, endDate);
    }

    /**
     * 이번 주 회의 데이터를 가져오는 메서드
     * @return
     */
    public List<MeetingDTO> getMeetingData4Week() {
        LocalDateTime monday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)).plusDays(3); //월요일
        LocalDateTime friday = LocalDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.FRIDAY)).plusDays(7); //금요일
        LocalDateTime startDate = LocalDateTime.of(monday.getYear(), monday.getMonth(), monday.getDayOfMonth(), 0,0,0);
        LocalDateTime endDate = LocalDateTime.of(friday.getYear(), friday.getMonth(), friday.getDayOfMonth(), 23,59,59);
        return getMeetingData4DateWithMemberStr(startDate, endDate);
    }

    /**
     * 전달된 기간에 회의 데이터를 가져오는 메서드
     * @param startDate
     * @param endDate
     * @return
     */
    private List<MeetingDTO> getMeetingData4Date(LocalDateTime startDate, LocalDateTime endDate) {
        return meetingRepoService.getMeeting4DuringStartEnd(startDate, endDate).stream()
                .map(meeting -> modelMapper.map(meeting, MeetingDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * 전달된 기간에 회의 데이터를 가져오며, 참석인원을 String 으로 변환해서 넣어주는 메서드
     * @param startDate
     * @param endDate
     * @return
     */
    private List<MeetingDTO> getMeetingData4DateWithMemberStr(LocalDateTime startDate, LocalDateTime endDate) {
        List<MeetingDTO> meetingList = getMeetingData4Date(startDate, endDate);
        for (MeetingDTO meeting : meetingList) {
            meeting.setAttendUserList(meeting.getAttendMemberList().stream()
                    .map(meetingMember -> meetingMember.getAttendOutMember()==null?meetingMember.getAttendCompanyMember().getName():meetingMember.getAttendOutMember())
                    .collect(Collectors.toList()));
        }
        return meetingList;
    }

    //////////////////////////
    //      Save Update
    //////////////////////////

    /**
     * 회의 데이터 저장 메서드
     * @param dto
     * @return
     */
    public JsonResponseVO saveData(MeetingDTO dto) {
        //시간을 체크해본다.
        int timeResultCode = checkMeetingTime(dto, dto.getStartDate(), dto.getEndDate());
        if (timeResultCode != START_END_PASS) { return makeTimeErrorJsonVo(timeResultCode); }

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
        //시간을 체크해본다.
        int timeResultCode = checkMeetingTime(dto, dto.getStartDate(), dto.getEndDate());
        if (timeResultCode != START_END_PASS) { return makeTimeErrorJsonVo(timeResultCode); }

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


    //////////////////////////
    //         Delete
    //////////////////////////

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


    //////////////////////////
    //      Support
    //////////////////////////

    /**
     * 회의 시작 시간 및 종료시간 중복을 확인해주는 메서드
     * @param startDate
     * @param endDate
     * @return
     */
    private int checkMeetingTime(MeetingDTO meetingDTO, LocalDateTime startDate, LocalDateTime endDate) {
        LocalDateTime targetStartDate = LocalDateTime.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth(), 0,0,0); //회의실 조회 시작일
        LocalDateTime targetEndDate = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23,59,59); //회의실 조회 종료일

        for (Meeting meeting : meetingRepoService.getMeeting4RoomWithTime(meetingDTO.getRoom(), targetStartDate, targetEndDate)) { //순회하며 점검
            if (meeting.getMeetingIdx() == meetingDTO.getMeetingIdx()) { continue; } //만약 업데이트의 경우 넘겨준다.

            if (startDate.isEqual(meeting.getStartDate())) { return START_SAME; }
            else if (endDate.isEqual(meeting.getEndDate())) { return END_SAME; }
            else if (startDate.isAfter(meeting.getStartDate()) && startDate.isBefore(meeting.getEndDate()) ) { return START_DURING; }
            else if (endDate.isAfter(meeting.getStartDate()) && endDate.isBefore(meeting.getEndDate()) ) { return END_DURING; }
            else if (meeting.getStartDate().isAfter(startDate) && meeting.getEndDate().isBefore(endDate)) { return START_END_INCLUDE; }
        }
        return START_END_PASS;
    }

    /**
     * 시간 확인 에러에 대한 Json 반환 값을 만들어주는 메서드
     * @param code
     * @return
     */
    private JsonResponseVO makeTimeErrorJsonVo(int code) {
        if (code == START_SAME) { return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_FAIL_SAME_START_TIME); }
        else if (code == END_SAME) { return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_FAIL_SAME_END_TIME); }
        else if (code == START_DURING) { return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_FAIL_START_TIME); }
        else if (code == END_DURING) { return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_FAIL_END_TIME); }
        else if (code == START_END_INCLUDE) { return new JsonResponseVO(JsonResponseEnum.MEETING_DATA_ADD_FAIL_INCLUDE_TIME); }
        return null;
    }

}
