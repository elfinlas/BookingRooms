package com.mhlab.br.service.repos;

import com.mhlab.br.jpa.entity.MeetingMember;
import com.mhlab.br.jpa.persistence.MeetingAttendMemberRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 회의 참석 인원 데이터를 처리하는 서비스 객체
 *
 * Created by MHLab on 05/11/2018..
 */

@Slf4j
@Service
public class MeetingAttendMemberRepoService {

    private MeetingAttendMemberRepo meetingAttendMemberRepo;

    public MeetingAttendMemberRepoService(MeetingAttendMemberRepo meetingAttendMemberRepo) {
        this.meetingAttendMemberRepo = meetingAttendMemberRepo;
    }

    /**
     * 회의 참석 인원 데이터 저장
     * @param meetingMember
     */
    public boolean saveData(MeetingMember meetingMember) {
        return meetingAttendMemberRepo.save(meetingMember) != null;
    }

}
