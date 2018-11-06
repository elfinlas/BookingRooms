package com.mhlab.br.domain.dto;

import com.mhlab.br.domain.enums.MeetingTypeEnum;
import com.mhlab.br.jpa.entity.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 회의록 데이터 DTO
 *
 * Created by MHLab on 29/10/2018.
 */

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class MeetingDTO {

    private int meetingIdx;
    private String title; //회의 제목
    private String content; //회의 내용
    private LocalDateTime startDate; //회의 시작 시간
    private LocalDateTime endDate; //회의 종료 시간
    private Boolean isPublic; //공개 여부
    private LocalDateTime createDate; //데이터 생성일
    private LocalDateTime updateDate; //데이터 수정일
    private MeetingTypeEnum meetingType;
    private Room room; //회의실
    private List<Integer> attendMemberList;


    //Client Only
    private List<String> attendUserList;

    /*
    @OneToMany(mappedBy = "meeting")
    private List<MeetingMember> attendMemberList; //회의 참석자
     */
}
