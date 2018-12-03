package com.mhlab.br.component.interceptors;

import com.mhlab.br.component.annotations.AdminOnly;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.utils.SessionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.mhlab.br.utils.SessionHelper.*;

/**
 * Created by MHLab on 29/11/2018..
 */

@Slf4j
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!hasSessionInAccount(request.getSession())) { //비 로그인 상태인 경우
            try { response.sendRedirect("/users/login"); }
            catch (IOException ie ) {} //만약 리다이렉션 도중 에러가 난 경우
            return false;
        }
        if (!checkAnnotation4Admin(request, response, handler)) { //관리자 체크 시 아닌 경우
            try { response.sendRedirect("/?result=noadmin"); }
            catch (IOException ie ) {} //만약 리다이렉션 도중 에러가 난 경우
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception arg3) throws Exception {

    }

    /**
     * 관리자 체크 어노테이션을 확인하는 메서드
     * @param request
     * @param response
     * @param handler
     * @return
     */
    private boolean checkAnnotation4Admin(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("a = " + handler.getClass().getName());


        HandlerMethod method = (HandlerMethod)handler;
        AdminOnly adminOnlyAnnotation = method.getMethodAnnotation(AdminOnly.class);
        if (adminOnlyAnnotation != null && adminOnlyAnnotation.isAdminOnly()) { //관리자 체크 여부를 확인하는 어노테이션이 있는 경우
            return SessionHelper.getSessionDataInAccount(request.getSession()).getId().equals("admin");
        }
        return true;
    }

}
