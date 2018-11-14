package com.mhlab.br.service.repos;

import com.mhlab.br.domain.dto.RoomDTO;
import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.domain.pages.PageMaker;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.jpa.persistence.RoomRepo;
import com.mhlab.br.service.abstracts.AbstractListPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회의실의 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class RoomRepoService extends AbstractListPageService {

    private RoomRepo roomRepo;

    @Autowired
    public RoomRepoService(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }


    //////////////////////////////
    //          Get
    //////////////////////////////


    /**
     * 하나의 데이터를 가져오는 메서드
     * @param idx
     * @return
     */
    public Room getRoomData4Idx(int idx) {
        return roomRepo.findById(idx).orElse(new Room());
    }

    /**
     * 모든 회의실 정보를 가져오는 메서드
     * @return
     */
    public List<Room> getAllRoomList() {
        return roomRepo.findAll();
    }


    /**
     *
     * @param criteria
     * @return
     */
    public List<Room> getAllRoomPageList(Criteria criteria) {
        return roomRepo.findAllBy(PageRequest.of(criteria.getPage(),criteria.getPerPageNum(), Sort.Direction.DESC, "createDate"));
    }

    /**
     * 
     * @param criteria
     * @return
     */
    public PageMaker getPageMaker(Criteria criteria) {
        return getPageMaker(roomRepo.countAllBy(), criteria);
    }


    /**
     * 데이터를 저장하는 메서드
     * @param data
     * @return
     */
    public boolean saveRoomData(Room data) {
        LocalDateTime now = LocalDateTime.now();
        data.setCreateDate(now);
        data.setUpdateDate(now);
        return roomRepo.save(data) != null;
    }

    /**
     * 데이터 업데이트 메서드
     * @param dto
     * @return
     */
    public boolean updateRoomData(RoomDTO dto) {
        Room data = getRoomData4Idx(dto.getRoomIdx());
        data.setName(dto.getName());
        data.setDescription(dto.getDescription());
        data.setUpdateDate(LocalDateTime.now());
        return roomRepo.save(data) != null;
    }


    /**
     * 데이터 삭제 메서드
     * @param idx
     */
    public void deleteRoomData(int idx) {
        roomRepo.deleteById(idx);
    }

}
