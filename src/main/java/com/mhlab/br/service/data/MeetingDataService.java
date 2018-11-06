package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.enums.MeetingTypeEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.MeetingMember;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.service.repos.MeetingRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 회의록 데이터를 처리하는 서비스 객체
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class MeetingDataService {


    private MeetingRepoService meetingRepoService;
    private AccountRepo accountRepo;

    public MeetingDataService(MeetingRepoService meetingRepoService, AccountRepo accountRepo) {
        this.meetingRepoService = meetingRepoService;
        this.accountRepo = accountRepo;
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
                .setMeetingType(MeetingTypeEnum.NORMAL)
                .setRoom(dto.getRoom());

        List<MeetingMember> attendMemberList = new ArrayList<>();

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
}
