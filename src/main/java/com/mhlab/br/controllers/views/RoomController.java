package com.mhlab.br.controllers.views;

import com.mhlab.br.component.annotations.AdminOnly;
import com.mhlab.br.domain.dto.RoomDTO;
import com.mhlab.br.domain.dto.RoomInMeetingDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.domain.pages.SearchCriteria;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.jpa.entity.Meeting;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.service.data.MeetingDataService;
import com.mhlab.br.service.data.RoomDataService;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 회의실 데이터를 처리하는 컨트롤러 로직
 *
 * Created by MHLab on 01/11/2018.
 */

@Slf4j
@Controller
@RequestMapping("room/*")
public class RoomController {

    private RoomDataService roomDataService;
    private RoomRepoService roomRepoService;
    private MeetingDataService meetingDataService;

    public RoomController(RoomDataService roomDataService, RoomRepoService roomRepoService, MeetingDataService meetingDataService) {
        this.roomDataService = roomDataService;
        this.roomRepoService = roomRepoService;
        this.meetingDataService = meetingDataService;
    }

    /**
     * 회의실 별 회의 데이터를 가져오는 메서드
     * @return
     */
    @GetMapping("meeting/time")
    public ModelAndView getRoomInMeeting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/room/meeting/list");
        return mv;
    }

    /**
     * 회의실에 따른 데이터를 가져오는 메서드
     * @param idx
     * @return
     */
    @ResponseBody
    @GetMapping("get/data/{idx}")
    public JsonResponseVO getRoomData(@PathVariable(name = "idx") Integer idx) {
        return roomDataService.getData4Idx(idx);
    }


    /**
     * 회의실 및 시간에 따른 데이터를 가져오는 메서드
     * @param roomStr
     * @param dateStr
     * @return
     */
    @ResponseBody
    @GetMapping("get/data/meeting/time/{room}/{date}")
    public JsonResponseVO getRoomInMeetingData(@PathVariable(name = "room") String roomStr, @PathVariable(name = "date") String dateStr) {
        return meetingDataService.getMeetingData4Room(LocalDate.parse(dateStr), roomDataService.getData4RoomStr(roomStr));
    }

    /**
     * 회의실 현황의 화면 데이터를 처리하는 메서드
     * @param criteria
     * @return
     */
    @AdminOnly
    @GetMapping("status/list")
    public ModelAndView getRoomStatusDataList(@ModelAttribute SearchCriteria criteria) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/room/status/list");
        mv.addObject("roomList", roomRepoService.getAllRoomPageList(criteria));
        mv.addObject("pageMaker", roomRepoService.getPageMaker(criteria));
        return mv;
    }

    /**
     * 회의실 데이터 등록
     * @param dto
     * @return
     */
    @AdminOnly
    @ResponseBody
    @PostMapping("add/data")
    public JsonResponseVO postRoomAdd(@RequestBody RoomDTO dto) {
        return roomDataService.saveData(dto);
    }


    /**
     * 회의실 데이터를
     * @param dto
     * @return
     */
    @AdminOnly
    @ResponseBody
    @PostMapping("update/data")
    public JsonResponseVO postRoomUpdate(@RequestBody RoomDTO dto) {
        return roomDataService.updateData(dto);
    }


    /**
     *
     * @param idx
     * @return
     */
    @AdminOnly
    @ResponseBody
    @PostMapping("delete/data/{idx}")
    public JsonResponseVO postRoomDelete(@PathVariable(name = "idx") Integer idx) {
        return roomDataService.deleteData(idx);
    }

}
