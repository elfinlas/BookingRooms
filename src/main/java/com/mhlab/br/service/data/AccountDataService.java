package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.LoginDTO;
import com.mhlab.br.domain.dto.SignUpDto;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.AutoLogin;
import com.mhlab.br.service.repos.AccountRepoService;
import com.mhlab.br.utils.CommonUtils;
import com.mhlab.br.utils.SecurityUtils;
import com.mhlab.br.utils.SessionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * 계정 데이터를 처리하는 서비스 객체
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class AccountDataService {

    public static final String AUTO_LOGIN_TOKEN_KEY = "BKAutoLoginToken";

    private AccountRepoService accountRepoService;

    public AccountDataService(AccountRepoService accountRepoService) {
        this.accountRepoService = accountRepoService;
    }


    /////////////////////////
    //  Login & Logout
    /////////////////////////

    /**
     * 자동 로그인을 사용하지 않고 일반적인 로그인으로 처리를 하는 메서드
     * @param request
     * @param response
     * @param dto
     * @return
     */
    public JsonResponseVO login4NonAutoLogin(HttpServletRequest request, HttpServletResponse response, LoginDTO dto) {
        if(dto.getLoginId().equals("system")) { return new JsonResponseVO(JsonResponseEnum.LOGIN_FAIL_USE_SYSTEM);  }
        else if(accountRepoService.checkIdAndNormalPw(dto.getLoginId(), dto.getLoginPw())) { //ID와 비밀번호가 맞는 경우
            Account account = accountRepoService.getAccountData4Id(dto.getLoginId());

            if (dto.isAutoLogin()) { //자동 로그인이 활성화 되어 있는 경우 alToken을 전달해준다.
                Cookie alCookie = makeAutoLoginCookie();

                //디비에 자동 로그인 정보를 저장한다.
                AutoLogin autoLogin = new AutoLogin()
                        .setId(dto.getLoginId())
                        .setIdentifier(request.getSession().getId())
                        .setToken(alCookie.getValue());
                accountRepoService.saveAutoLoginData(autoLogin);

                //응답에 쿠키 전송
                response.addCookie(alCookie);
            }
            return login(request.getSession(), account);
        }
        else { return new JsonResponseVO(JsonResponseEnum.LOGIN_FAIL);  }
    }

    public JsonResponseVO login4AutoLogin(HttpServletRequest request,HttpServletResponse response, String token) {
        //만약 토큰이 존재하는 경우 기존 토큰은 삭제 한다.
        String targetId = accountRepoService.getAutoLoginData4Token(token).map(autoLogin -> {
            accountRepoService.deleteAutoLoginData(autoLogin);
            return autoLogin.getId();
        }).orElse("");

        if (!targetId.equals("")) {
            //새로운 토큰을 생성한다.
            Cookie alCookie = makeAutoLoginCookie();

            //디비에 자동 로그인 정보를 저장한다.
            AutoLogin autoLogin = new AutoLogin()
                    .setId(targetId)
                    .setIdentifier(request.getSession().getId())
                    .setToken(alCookie.getValue());
            accountRepoService.saveAutoLoginData(autoLogin);

            //새로 갱신된 토큰을 전송
            response.addCookie(alCookie);
            return login(request.getSession(), accountRepoService.getAccountData4Id(targetId));
        }
        else { return new JsonResponseVO(JsonResponseEnum.LOGIN_FAIL); } //로그인 실패 시 -> 처리 보강해야 함
    }

    /**
     *
     * @param dto
     * @return
     */
    public JsonResponseVO signUpAccountData(SignUpDto dto) {
        if (accountRepoService.getAccountData4Id(dto.getSignUpId()) != null ) {
            return new JsonResponseVO(JsonResponseEnum.SIGN_UP_FAIL_EXIST_ID);
        }
        else if (dto.getSignUpId().contains("admin") || dto.getSignUpId().contains("system")) { //시스템 사용 체크
            return new JsonResponseVO(JsonResponseEnum.SIGN_UP_FAIL_USE_SYSTEM_ID);
        }
        else {
            Account targetAccount = new Account()
                    .setId(dto.getSignUpId())
                    .setName(dto.getSignUpName())
                    .setPw(SecurityUtils.encryptData4SHA(dto.getSignUpPw()))
                    .setTeamName(dto.getTeamName());
            accountRepoService.saveAccountData(targetAccount);
            return new JsonResponseVO(JsonResponseEnum.SIGN_UP_SUCCESS);
        }
    }



    /**
     * (내부용) 실질적으로 로그인 처리를 진행하는 내부 메서드
     * @param session
     * @param account
     * @return
     */
    private JsonResponseVO login(HttpSession session, Account account) {
        SessionHelper.setSessionDataInAccount(session, account);
        if (account.getId().equals("admin")) { return new JsonResponseVO(JsonResponseEnum.LOGIN_SUCCESS_ADMIN); }
        else { return new JsonResponseVO(JsonResponseEnum.LOGIN_SUCCESS); }
    }

    /**
     * (내부용) 자동 로그인 쿠키를 만들어주는 메서드
     * @return
     */
    private Cookie makeAutoLoginCookie() {
        String alToken = CommonUtils.makeUUID2String(); //UUID
        Cookie alTokenCookie = new Cookie(AUTO_LOGIN_TOKEN_KEY, alToken);
        alTokenCookie.setPath("/");
        alTokenCookie.setMaxAge(60*60*24*7);
        return alTokenCookie;
    }

    //로그아웃
    public JsonResponseVO logout(HttpServletRequest request, HttpServletResponse response) {
        Account targetAccount = SessionHelper.getSessionDataInAccount(request.getSession());
        if(targetAccount != null) {
            //자동 로그인 상태 체크
            Cookie alCookie = WebUtils.getCookie(request, AUTO_LOGIN_TOKEN_KEY);

            if(alCookie != null) { //자동 로그인인 경우 자동 로그인에 사용된 값은 삭제해준다.
                //토큰이 존재하는 경우 삭제 처리를 한다.
                accountRepoService.getAutoLoginData4Token(alCookie.getValue())
                        .ifPresent(autoLogin -> accountRepoService.deleteAutoLoginData(autoLogin));

                //자동 로그인 쿠기를 무효화 시킨다.
                Cookie invalidCookie = new Cookie(AUTO_LOGIN_TOKEN_KEY, null);
                invalidCookie.setPath("/");
                invalidCookie.setMaxAge(0);

                response.addCookie(invalidCookie);
            }

            //세션을 무효화 시킨다.
            request.getSession().invalidate();
            return new JsonResponseVO(JsonResponseEnum.LOGOUT_SUCCESS);
        }
        else { return new JsonResponseVO(JsonResponseEnum.LOGOUT_FAIL); } //세션에 로그인 정보가 없는 경우
    }
}
