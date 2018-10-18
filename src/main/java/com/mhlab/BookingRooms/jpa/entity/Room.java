package com.mhlab.BookingRooms.jpa.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

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
    private int idx;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;
}
