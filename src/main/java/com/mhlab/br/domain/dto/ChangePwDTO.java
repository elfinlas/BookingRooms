package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 사용자 암호 초기화 시 사용하는 DTO
 *
 * Created by MHLab on 06/12/2018.
 */

@Getter
@Setter
@Accessors(chain = true)
@ToString
public class ChangePwDTO {
    private String beforePw;
    private String afterPw;
    private String validPw;
}
