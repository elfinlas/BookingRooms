package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.RoomDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.service.repos.MeetingRepoService;
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
    private MeetingRepoService meetingRepoService;

    public RoomDataService(RoomRepoService roomRepoService, MeetingRepoService meetingRepoService) {
        this.roomRepoService = roomRepoService;
        this.meetingRepoService = meetingRepoService;
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
     * 회의실 업데이트 메서드
     * @param dto
     * @return
     */
    public JsonResponseVO updateData(RoomDTO dto) {
        roomRepoService.updateRoomData(dto);
        return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_UPDATE_SUCCESS);
    }

    /**
     * 회의실 삭제 메서드
     * @param idx
     * @return
     */
    public JsonResponseVO deleteData(int idx) {
        if(roomRepoService.getAllRoomList().size() == 1) { return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_DELETE_FAIL_ONLY_ONE); }
        else if (meetingRepoService.getMeeting4Room(roomRepoService.getRoomData4Idx(idx)).size() != 0) {
            return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_DELETE_FAIL_HAS_MEETING_DATA);
        }
        //else if
        roomRepoService.deleteRoomData(idx);
        return new JsonResponseVO(JsonResponseEnum.ROOM_DATA_DELETE_SUCCESS);
    }
}
