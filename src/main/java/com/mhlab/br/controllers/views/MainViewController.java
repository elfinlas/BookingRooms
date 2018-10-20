package com.mhlab.br.controllers.views;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by MHLab on 17/10/2018.
 */

@Slf4j
@Controller
public class MainViewController {

    @GetMapping("/")
    public ModelAndView getMain() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/main");
        return mv;
    }

}
