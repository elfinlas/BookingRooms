package com.mhlab.br.service.repos;

import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.domain.pages.PageMaker;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.MeetingRepo;
import com.mhlab.br.service.abstracts.AbstractListPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
public class MeetingRepoService extends AbstractListPageService {

    private MeetingRepo meetingRepo;


    @Autowired
    public MeetingRepoService(MeetingRepo meetingRepo) {
        this.meetingRepo = meetingRepo;
    }

    //////////////////////////////
    //        Get Paging
    //////////////////////////////


    /**
     * 회의 데이터 전체를 페이징으로 가져오는 메서드
     * @param criteria
     * @return
     */
    public List<Meeting> getAllMeetingPageList(Criteria criteria) {
        return meetingRepo.findAllBy(PageRequest.of(criteria.getPage(), criteria.getPerPageNum(), Sort.Direction.DESC, "createDate"));
    }


    /**
     * 회의 데이터의 PageMaker 객체를 가져오는 메서드
     * @param criteria
     * @return
     */
    public PageMaker getPageMaker(Criteria criteria) {
        return getPageMaker(meetingRepo.countAllBy(), criteria);
    }




    //////////////////////////////
    //          Get
    //////////////////////////////

    /**
     * idx로 하나 가져오는 메서드
     * @param idx
     * @return
     */
    public Meeting getMeeting4Idx(int idx) {
        return meetingRepo.getOne(idx);
    }


    /**
     * 특정 회의실에서 진행 및 시작, 종료 시간에 맞는 데이터를 가져오는 메서드
     * @param roomList
     * @param start
     * @param end
     * @return
     */
    public List<Meeting> getMeeting4RoomList(List<Room> roomList, LocalDateTime start, LocalDateTime end) {
        return roomList.stream()
                .map(room -> meetingRepo.findByRoomAndStartDateAfterAndEndDateBeforeOrderByStartDate(room, start, end))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }


    /**
     * 특정 회의실에서 진행 및 시작, 종료 시간에 맞는 데이터를 가져오는 메서드
     * @param room
     * @param start
     * @param end
     * @return
     */
    public List<Meeting> getMeeting4Room(Room room, LocalDateTime start, LocalDateTime end) {
        return meetingRepo.findByRoomAndStartDateAfterAndEndDateBeforeOrderByStartDate(room, start, end);
    }


    /**
     * 회의 시작 및 종료 시간에 맞는 데이터를 가져오는 메서드
     * @param start
     * @param end
     * @return
     */
    public List<Meeting> getMeeting4DuringStartEnd(LocalDateTime start, LocalDateTime end) {
        return meetingRepo.findByStartDateAfterAndEndDateBeforeOrderByStartDate(start, end);
    }

    /**
     * 데이터를 저장하는 메서드
     * @param data
     */
    public Meeting saveData(Meeting data) {
        LocalDateTime now = LocalDateTime.now();
        data.setCreateDate(now);
        data.setUpdateDate(now);
        meetingRepo.save(data);
        return data;
    }

    /**
     * 데이터를 수정하는 메서드
     * @param data
     */
    public void updateData(Meeting data) {
        data.setUpdateDate(LocalDateTime.now());
        meetingRepo.save(data);
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
        //회의 데이터 삭제
        meetingRepo.delete(data);
    }
}
