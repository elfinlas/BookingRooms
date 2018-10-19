package com.mhlab.br.utils;

import java.util.UUID;

/**
 * 개발에 필요한 일반적인 유틸리티 성향의 기능을 제공하는 클래스
 *
 * Created by MHLab on 19/10/2018.
 */
public class CommonUtils {

    /**
     * UUID String을 만들어 리턴한다.
     * @return String으로 변환된 UUID
     */
    public static String makeUUID2String() {
        return UUID.randomUUID().toString();
    }
}
