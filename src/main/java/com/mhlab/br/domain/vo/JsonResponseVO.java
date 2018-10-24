package com.mhlab.br.domain.vo;

import com.mhlab.br.domain.enums.JsonResponseEnum;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Json 데이터 반환 시 사용하는 클래스
 *
 * Created by MHLab on 22/10/2018..
 */

@ToString
@Accessors(chain = true)
@Getter
public class JsonResponseVO {
    private int resultCode;
    private Object resultMsg;
    public JsonResponseVO(JsonResponseEnum jsonResponseEnum) {
        this.resultCode = jsonResponseEnum.code();
        this.resultMsg = jsonResponseEnum.msg();
    }
    public JsonResponseVO(JsonResponseEnum jsonResponseEnum, Object msg) {
        this.resultCode = jsonResponseEnum.code();
        this.resultMsg = msg!=null?msg:jsonResponseEnum.msg();
    }
}
