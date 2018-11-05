package com.mhlab.br.domain.enums;

/**
 * Json 응답 코드를 정의한 Enum
 *
 * Created by MHLab on 22/10/2018..
 */

public enum JsonResponseEnum {

    //Meeting
    MEETING_BEFORE_RES_SUCCESS(100, "Ok"),
    MEETING_BEFORE_RES_FAIL(-100, "Fail"),

    MEETING_DATA_ADD_SUCCESS(110, "Success"),

    SYSTEM_TEST(99999, "Test Ok");

    private int code;
    private String msg;
    JsonResponseEnum(int code, String msg) { this.code = code; this.msg = msg; }
    public int code() {return this.code;}
    public String msg() {return this.msg;}
}
