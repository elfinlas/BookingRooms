package com.mhlab.br.service.repos;

import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.RoomRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 회의실의 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class RoomRepoService {

    private RoomRepo roomRepo;

    @Autowired
    public RoomRepoService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }


    //////////////////////////////
    //          Get
    //////////////////////////////

    /**
     * 모든 회의실 정보를 가져오는 메서드
     * @return
     */
    public List<Room> getAllRoomList() {
        return roomRepo.findAll();
    }


}
