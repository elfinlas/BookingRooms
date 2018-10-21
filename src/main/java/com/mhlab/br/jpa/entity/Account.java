package com.mhlab.br.jpa.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 회원 데이터 엔티티
 *
 * Created by MHLab on 18/10/2018.
 */

@Getter @Setter
@Accessors(chain = true)
@Entity
@JsonIgnoreProperties({"pw"}) //암호의 경우 사용할 일이 없기 때문에 Json 변환에서 제외 처리를 한다.
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int accountIdx;

    @Column(nullable = false)
    private String id; //계정명

    @Column(nullable = false)
    private String pw; //암호

    @Column(nullable = false)
    private String name; //사용자 이름

    @Column(nullable = false)
    private String teamName; //부서명

    @Column(nullable = false)
    private LocalDateTime createDate; //데이터 생성일

    @Column(nullable = false)
    private LocalDateTime updateDate; //데이터 수정일

}
