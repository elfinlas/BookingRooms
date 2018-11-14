package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Meeting Entity를 처리하는 리포지토리 객체
 *
 * Created by MHLab on 18/10/2018.
 */

@Repository
public interface MeetingRepo extends JpaRepository<Meeting, Integer> {


    /////////////////////////
    //    WithOut Paging
    /////////////////////////

    /**
     * 특정 회의실에 시작, 종료 시간에 맞는 데이터를 가져오는 메서드
     * @param room
     * @param startDate
     * @param endDate
     * @return
     */
    List<Meeting> findByRoomAndStartDateAfterAndEndDateBeforeOrderByStartDate(Room room, LocalDateTime startDate, LocalDateTime endDate);


    /**
     * 특정 시작 및 종료시간에 포함되는 데이터를 가져오는 메서드
     * @param startDate
     * @param endDate
     * @return
     */
    List<Meeting> findByStartDateAfterAndEndDateBeforeOrderByStartDate(LocalDateTime startDate, LocalDateTime endDate);


    /////////////////////////
    //     With Paging
    /////////////////////////

    /**
     * 회의 데이터 전체를 가져오는 메서드
     * @param pageable
     * @return
     */
    List<Meeting> findAllBy(Pageable pageable);

    /**
     * 회의 데이터 전체 카운트 값
     * @return
     */
    int countAllBy();

}
