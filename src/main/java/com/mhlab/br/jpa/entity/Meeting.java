package com.mhlab.br.jpa.entity;

import com.mhlab.br.config.BooleanToYesNoConverter;
import com.mhlab.br.domain.enums.MeetingTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 회의 정보를 저장하는 엔티티
 *
 * Created by MHLab on 18/10/2018.
 */

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Meeting {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int meetingIdx;

    @Column(nullable = false)
    private String title; //회의 제목

    @Column(nullable = false)
    private String content; //회의 내용

    @Column(nullable = false)
    private LocalDateTime startDate; //회의 시작 시간

    @Column(nullable = false)
    private LocalDateTime endDate; //회의 종료 시간

    @Column(nullable = false)
    private LocalDateTime createDate; //데이터 생성일

    @Column(nullable = false)
    private LocalDateTime updateDate; //데이터 수정일

    @Column
    @Convert(converter = BooleanToYesNoConverter.class)
    private boolean isPublic; //공개 여부

    @Column
    @Enumerated(value = EnumType.STRING)
    private MeetingTypeEnum meetingType; //회의 타입


    @OneToOne
    @JoinColumn(name = "room")
    private Room room; //회의실

    @OneToMany(mappedBy = "meeting")
    private List<MeetingAttendMember> attendMemberList; //회의 참석자

}
