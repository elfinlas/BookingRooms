package com.mhlab.br.component.interceptors;

import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.service.data.AccountDataService;
import com.mhlab.br.service.repos.AccountRepoService;
import com.mhlab.br.utils.SessionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MHLab on 01/12/2018..
 */

@Slf4j
@Component
public class AutoLoginInterceptor extends HandlerInterceptorAdapter {

    private AccountDataService accountDataService;
    private AccountRepoService accountRepoService;

    public AutoLoginInterceptor(AccountDataService accountDataService, AccountRepoService accountRepoService) {
        this.accountDataService = accountDataService;
        this.accountRepoService = accountRepoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie autoLoginTokenCookie = WebUtils.getCookie(request, AccountDataService.AUTO_LOGIN_TOKEN_KEY);

        if(autoLoginTokenCookie != null) { //자동 로그인 쿠키가 있다면
            if(accountRepoService.getAutoLoginData4Token(autoLoginTokenCookie.getValue()).isPresent()) { //이상이 없는 경우
                JsonResponseVO jrVo = accountDataService.login4AutoLogin(request, response, autoLoginTokenCookie.getValue());

                if (jrVo.getResultCode()== JsonResponseEnum.LOGIN_SUCCESS.code()) { //일반 로그인
                    try { response.sendRedirect("/"); }
                    catch (IOException ie ) { log.error("ie = " + ie.getMessage()); } //만약 리다이렉션 도중 에러가 난 경우
                }
                else if (jrVo.getResultCode()==JsonResponseEnum.LOGIN_SUCCESS_ADMIN.code()) { //관리자 로그인
                    try { response.sendRedirect("/admin/"); }
                    catch (IOException ie ) { log.error("ie = " + ie.getMessage()); } //만약 리다이렉션 도중 에러가 난 경우
                }
                else { //로그인 실패로 넘긴다. -> 보강 필요
                    try { response.sendRedirect("/users/login"); }
                    catch (IOException ie ) { log.error("ie = " + ie.getMessage()); } //만약 리다이렉션 도중 에러가 난 경우
                }
            }

            return false;
        }
        else { //토큰이 존재하지 않는 경우 해당 세션으로 접근한 것을 무효화
            if(!SessionHelper.hasSessionInAccount(request.getSession())) { //세션에 값이 없을 때만 로그아웃 처리를 진행한다.
                request.getSession().invalidate(); //현재 세션을 무효화 시킨다.

                //응답 쿠키를 무효화 시킨다.
                Cookie invalidCookie = new Cookie(AccountDataService.AUTO_LOGIN_TOKEN_KEY, null);
                invalidCookie.setPath("/");
                invalidCookie.setMaxAge(0);

                response.addCookie(invalidCookie);
            }
        }
        return true;
    }
}
