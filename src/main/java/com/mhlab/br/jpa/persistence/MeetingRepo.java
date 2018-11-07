package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.Room;
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
     *
     * @param room
     * @return
     */
    List<Meeting> findByRoom(Room room);


    //List<ScheduleData> findByStartDateAfterAndEndDateBeforeOrderByStartDate(LocalDateTime startDate, LocalDateTime endDate);

    List<Meeting> findByRoomAndStartDateAfterAndEndDateBeforeOrderByStartDate(Room room, LocalDateTime startDate, LocalDateTime endDate);



    /////////////////////////
    //     With Paging
    /////////////////////////
}
