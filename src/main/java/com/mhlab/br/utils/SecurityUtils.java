package com.mhlab.br.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 보안과 관련된 공용 유틸리티를 제공하는 클래스
 *
 * Created by MHLab on 19/10/2018.
 */
public class SecurityUtils {

    /////////////////////////
    //      SHA-512
    /////////////////////////

    /**
     * 전달된 문자열을 SHA-512로 해싱해주는 메서드
     * @param normalData
     * @return
     */
    public static String encryptData4SHA(String normalData) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(normalData.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : md.digest()) { sb.append(Integer.toHexString(0xff & b)); }
            return sb.toString();
        }catch (NoSuchAlgorithmException nae) {
            return "";
        }
    }
}
