package com.mhlab.br.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by MHLab on 30/11/2018..
 */

@Entity
@Accessors(chain = true)
@Getter
@Setter
@Table(name = "auto_login")
public class AutoLogin {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int autoIdx;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String identifier;

    @Column(nullable = false)
    private LocalDateTime createDate; //생성 시각
}
