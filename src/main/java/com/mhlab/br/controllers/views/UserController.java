package com.mhlab.br.controllers.views;

import com.mhlab.br.component.annotations.AdminOnly;
import com.mhlab.br.domain.dto.LoginDTO;
import com.mhlab.br.domain.dto.SignUpDto;
import com.mhlab.br.domain.pages.SearchCriteria;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.service.data.AccountDataService;
import com.mhlab.br.utils.SessionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by MHLab on 29/11/2018..
 */

@Slf4j
@Controller
@RequestMapping(value = "users/*")
public class UserController {

    private AccountDataService accountDataService;

    public UserController(AccountDataService accountDataService) {
        this.accountDataService = accountDataService;
    }

    @AdminOnly
    @GetMapping("list")
    public ModelAndView getUserList(@ModelAttribute SearchCriteria criteria) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/users/list");
        mv.addObject("userList", accountDataService.getAllAccountPageList(criteria));
        mv.addObject("pageMaker", accountDataService.getPageMaker(criteria));
        return mv;
    }

    @GetMapping("login")
    public String getLogin(HttpServletRequest request, HttpServletResponse response) {
        if(SessionHelper.getSessionDataInAccount(request.getSession()) != null) { //만약 로그인 상태라면 메인으로 이동한다.
            try {  response.sendRedirect("/"); }
            catch (IOException e) { log.error("e = " + e.getMessage()); }
        }
        return "views/users/login";
    }


    @GetMapping("login/{msg}")
    public String getLogin(RedirectAttributes ra, @PathVariable String msg) {
        ra.addFlashAttribute("msg", msg);
        return "redirect:/users/login";
    }

    @PostMapping("login")
    @ResponseBody
    public JsonResponseVO loginPost(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginDTO dto) {
        return accountDataService.login4NonAutoLogin(request, response, dto);
    }

    @PostMapping("logout")
    @ResponseBody
    public JsonResponseVO logout(HttpServletRequest request, HttpServletResponse response) {
        return accountDataService.logout(request, response);
    }

    @PostMapping("signup")
    @ResponseBody
    public JsonResponseVO post4SignUp(@RequestBody SignUpDto dto) {
        return accountDataService.signUpAccountData(dto);
    }


    @ResponseBody
    @PostMapping("update/account/reset/pw/{idx}")
    public JsonResponseVO post4ResetAccountPw(@PathVariable(name = "idx") Integer idx) {
        return accountDataService.resetAccountPw(idx);
    }

    @ResponseBody
    @PostMapping("delete/account/{idx}")
    public JsonResponseVO post4DeleteAccount(@PathVariable(name = "idx") Integer idx) {
        return accountDataService.deleteAccount(idx);
    }

    @ResponseBody
    @PostMapping("update/account/info")
    public JsonResponseVO post4UpdateAccount(@RequestBody SignUpDto dto) {
        return accountDataService.updateAccount(dto);
    }

}
