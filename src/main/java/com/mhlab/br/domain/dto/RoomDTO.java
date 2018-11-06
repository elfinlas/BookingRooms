package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 회의실 데이터를 담을 DTO
 *
 * Created by MHLab on 05/11/2018.
 */


@Getter
@Setter
@Accessors(chain = true)
public class RoomDTO {

    private int roomIdx;
    private String name;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
