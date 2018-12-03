package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.RoomDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 회의실 데이터를 처리하는 서비스 객체
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class RoomDataService {

    private RoomRepoService roomRepoService;

    public RoomDataService(RoomRepoService roomRepoService) {
        this.roomRepoService = roomRepoService;
    }


    ////////////////////////
    //
    ////////////////////////

    /**
     * 인덱스에 맞는 회의실 데이터를 가져오는 메서드
     * @param idx
     * @return
     */
    public JsonResponseVO getData4Idx(int idx) {
        return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_GET_SUCCESS, roomRepoService.getRoomData4Idx(idx));
    }


    /**
     *
     * @param roomStr
     * @return
     */
    public List<Room> getData4RoomStr(String roomStr) {
        if (roomStr.toLowerCase().equals("a")) { return roomRepoService.getAllRoomList(); } //a 인 경우 전체 가져옴
        else {
            return Arrays.stream(roomStr.split(","))
                    .map(Integer::parseInt)
                    .map(idx -> roomRepoService.getRoomData4Idx(idx)).
                            collect(Collectors.toList());
        }
    }




    /**
     * 회의실 데이터를 추가하는 메서드
     * @param dto
     * @return
     */
    public JsonResponseVO saveData(RoomDTO dto) {
        Room room = new Room()
                .setName(dto.getName())
                .setDescription(dto.getDescription());
        roomRepoService.saveRoomData(room);
        return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_ADD_SUCCESS);
    }

    /**
     *
     * @param dto
     * @return
     */
    public JsonResponseVO updateData(RoomDTO dto) {
        roomRepoService.updateRoomData(dto);
        return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_UPDATE_SUCCESS);
    }

    /**
     *
     * @param idx
     * @return
     */
    public JsonResponseVO deleteData(int idx) {
        roomRepoService.deleteRoomData(idx);
        return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_DELETE_SUCCESS);
    }
}
