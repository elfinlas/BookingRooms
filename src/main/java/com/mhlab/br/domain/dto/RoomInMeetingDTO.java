package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 회의실의 회의 내역을 가져오는 DTO
 *
 * Created by MHLab on 07/11/2018.
 */

@Getter
@Setter
@Accessors(chain = true)
public class RoomInMeetingDTO {

    private List<RoomDTO> roomList;
    private List<MeetingDTO> meetingList;
}
