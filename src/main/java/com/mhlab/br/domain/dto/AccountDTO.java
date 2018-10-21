package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 계정 데이터 DTO
 *
 * Created by MHLab on 22/10/2018..
 */

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class AccountDTO {
    private int accountIdx;
    private String id;
    private String name;
    private String teamName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
