package com.mhlab.br.service.repos;

import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.persistence.MeetingRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 회의록 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class MeetingRepoService {

    private MeetingRepo meetingRepo;


    @Autowired
    public MeetingRepoService(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }


    //////////////////////////////
    //          Get
    //////////////////////////////



    //////

    public boolean saveData(Meeting data) {
        LocalDateTime now = LocalDateTime.now();
        data.setCreateDate(now);
        data.setUpdateDate(now);



        return meetingRepo.save(data) != null;
    }

}
