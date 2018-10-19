package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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






    /////////////////////////
    //     With Paging
    /////////////////////////
}
