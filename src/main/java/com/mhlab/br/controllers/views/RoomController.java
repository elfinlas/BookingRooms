package com.mhlab.br.controllers.views;

import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.service.data.RoomDataService;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * 회의실 데이터를 처리하는 컨트롤러 로직
 *
 * Created by MHLab on 01/11/2018.
 */

@Slf4j
@Controller
@RequestMapping("room/*")
public class RoomController {

    private RoomRepoService roomRepoService;
    private RoomDataService roomDataService;


    public RoomController(RoomRepoService roomRepoService, RoomDataService roomDataService) {
        this.roomRepoService = roomRepoService;
        this.roomDataService = roomDataService;
    }

    /**
     * 회의실 현황의 화면 데이터를 처리하는 메서드
     * @param criteria
     * @return
     */
    @GetMapping("status/list")
    public ModelAndView getRoomStatusDataList(@ModelAttribute Criteria criteria) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/room/status/list");
        List<Room> list = roomRepoService.getAllRoomPageList(criteria);
        log.info("list = " + list);
        for (Room room : list) {
            log.info("room = " + room.getName());
        }
        return mv;
    }


    /**
     * 회의실 히스토리 데이터를 처리하는 메서드
     * @param criteria
     * @return
     */
    @GetMapping("")
    public ModelAndView getRoomHistoryList(@ModelAttribute Criteria criteria) {
        ModelAndView mv = new ModelAndView();
        return mv;
    }

}
