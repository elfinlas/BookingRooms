package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Room Entity를 처리하는 리포지토리 객체
 *
 * Created by MHLab on 18/10/2018.
 */

@Repository
public interface RoomRepo extends JpaRepository<Room, Integer> {

    /////////////////////////
    //    WithOut Paging
    /////////////////////////




    /////////////////////////
    //     With Paging
    /////////////////////////

    /**
     * 회의실 전체를 페이징 처리하여 가져오는 메서드
     * @param pageable
     * @return
     */
    List<Room> findAllBy(Pageable pageable);

    /**
     * 회의실 전체 갯수를 가져오는 메서드
     * @return
     */
    int countAllBy();

}
