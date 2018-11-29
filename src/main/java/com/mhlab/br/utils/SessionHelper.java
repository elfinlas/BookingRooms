package com.mhlab.br.utils;

import com.mhlab.br.jpa.entity.Account;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * HttpSession 관련 처리 클래스
 *
 * Created by MHLab on 2017. 11. 30..
 */

@Slf4j
public class SessionHelper {

    public static final String S_IN_ACCOUNT = "Account"; //세션에 저장된 계정
    public static final String S_IN_TEMP_FILE = "TempFile"; //세션에 저장된 임시 파일 컬렉션
    public static final String S_IN_TEMP_DEL_FILE = "TempDelFile"; //세션에 저장된 임시 삭제 예정 파일 인덱스
    public static final String S_IN_NEW_CONTENTS = "NewContents"; //세션에 저장된 새로운 컨텐츠 카테고리


    /**
     * Session에 계정의 값이 존재하는지 체크하는 메서드 (기본 체크 처리)
     * @param session
     * @return
     */
    public static boolean hasSessionInAccount(HttpSession session) {
        return Optional.ofNullable(session.getAttribute(S_IN_ACCOUNT))
                .map(o -> true) //세션이 존재할 경우
                .orElse(false); //세션이 존재하지 않는 경우
    }


    /**
     * Session에 저장된 계정 정보를 가져온다.
     * @param session
     * @return
     */
    public static Account getSessionDataInAccount(HttpSession session) {
        return (Account)session.getAttribute(S_IN_ACCOUNT);
    }

    /**
     * 세션에 계정 정보를 주입하는 메서드
     * @param session
     * @param account
     */
    public static void setSessionDataInAccount(HttpSession session, Account account) {
        session.setAttribute(S_IN_ACCOUNT, account);
    }


}
