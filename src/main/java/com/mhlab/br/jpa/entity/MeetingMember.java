package com.mhlab.br.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * 회의 참석자 정보 엔티티
 *
 * Created by MHLab on 19/10/2018.
 */

@Getter
@Setter
@Accessors(chain = true)
@Entity
public class MeetingMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int attendIdx;

    @Column
    private String attendOutMember; //회의 참석한 외부 인력

    @ManyToOne
    @JoinColumn(name = "accountIdx", foreignKey = @ForeignKey(name = "FK_ACCOUNT"))
    private Account attendCompanyMember; //회의 참석한 내부 인력

    @ManyToOne
    @JoinColumn(name = "meetingIdx", foreignKey = @ForeignKey(name = "FK_MEETING"))
    private Meeting meeting; //회의 데이터

}
