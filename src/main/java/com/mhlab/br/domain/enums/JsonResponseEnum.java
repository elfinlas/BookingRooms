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

    MEETING_DATA_ADD_SUCCESS(110, "Success"), //회의 데이터 등록 성공
    MEETING_DATA_ADD_FAIL_SAME_START_TIME(-110, "Same start time"),
    MEETING_DATA_ADD_FAIL_SAME_END_TIME(-111, "Same end time"),
    MEETING_DATA_ADD_FAIL_START_TIME(-112, "Start time error"),
    MEETING_DATA_ADD_FAIL_END_TIME(-113, "End time error"),
    MEETING_DATA_ADD_FAIL_INCLUDE_TIME(-114, "Include time error"),
    MEETING_DATA_UPDATE_SUCCESS(111, "Success"),
    MEETING_DATA_DELETE_SUCCESS(112, "Success"),
    MEETING_DATA_GET_SUCCESS(113, "Success"),

    MEETING_CAL_DATA_GET_SUCCESS(120, "Success"), //회의 시작, 종료 시간의 데이터 조회 성공


    //Room
    ROOM_DATA_GET_SUCCESS(200, "success"),
    ROOM_DATA_ADD_SUCCESS(201, "success"),
    ROOM_DATA_UPDATE_SUCCESS(202, "success"),
    ROOM_DATA_DELETE_SUCCESS(203, "success"),

    ROOM_MEETING_DATA_GET_SUCCESS(210, "success"), //회의실별 회의 데이터 조회 성공

    SYSTEM_TEST(99999, "Test Ok");

    private int code;
    private String msg;
    JsonResponseEnum(int code, String msg) { this.code = code; this.msg = msg; }
    public int code() {return this.code;}
    public String msg() {return this.msg;}
}
