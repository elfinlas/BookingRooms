package com.mhlab.br.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by MHLab on 18/10/2018.
 */


@Getter
@Setter
@Accessors(chain = true)
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roomIdx;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private LocalDateTime createDate;

    @Column
    private LocalDateTime updateDate;
}
