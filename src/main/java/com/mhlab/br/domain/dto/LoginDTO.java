package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Created by MHLab on 2018. 3. 7...
 */

@Accessors(chain = true)
@Getter
@Setter
@ToString
public class LoginDTO {
    private String loginId;
    private String loginPw;
    private boolean autoLogin;
}
