package com.mhlab.br.controllers.views;

import com.mhlab.br.service.data.MeetingDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;

/**
 * Created by MHLab on 17/10/2018.
 */

@Slf4j
@Controller
public class MainViewController {

    private MeetingDataService meetingDataService;

    public MainViewController(MeetingDataService meetingDataService) {
        this.meetingDataService = meetingDataService;
    }

    @GetMapping("/")
    public ModelAndView getMain() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("today", LocalDateTime.now());
        mv.addObject("todayList", meetingDataService.getMeetingData4Today());
        mv.addObject("weekList", meetingDataService.getMeetingData4Week());
        mv.setViewName("views/main");
        return mv;
    }

}
