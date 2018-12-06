package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Created by MHLab on 2018. 7. 3..
 */

@Getter
@Setter
@Accessors(chain = true)
public class SignUpDto {
    private int signUpIdx; //업데이트 시 사용할 사용자 idx
    private String signUpId; //사용자 ID
    private String signUpName; //사용자 이름
    private String teamName; //부서명
    private String signUpPw; //사용자 암호
}
