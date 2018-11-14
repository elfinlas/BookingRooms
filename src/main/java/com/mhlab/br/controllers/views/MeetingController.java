package com.mhlab.br.controllers.views;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.pages.SearchCriteria;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.service.data.MeetingDataService;
import com.mhlab.br.service.data.RoomDataService;
import com.mhlab.br.service.repos.AccountRepoService;
import com.mhlab.br.service.repos.MeetingRepoService;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 미팅 데이터를 다루는 컨트롤러
 *
 * Created by MHLab on 22/10/2018..
 */

@Slf4j
@Controller
@RequestMapping("meeting/*")
public class MeetingController {

    private AccountRepoService accountRepoService;
    private RoomRepoService roomRepoService;
    private MeetingDataService meetingDataService;

    public MeetingController(AccountRepoService accountRepoService, RoomRepoService roomRepoService, MeetingDataService meetingDataService) {
        this.accountRepoService = accountRepoService;
        this.roomRepoService = roomRepoService;
        this.meetingDataService = meetingDataService;
    }


    /**
     *  자료 추가 및 업데이트 시 회의실 및 계정 데이터를 가져오는 메서드
     * @return
     */
    @GetMapping("get/add_res")
    @ResponseBody
    public JsonResponseVO getAddData4Resource() {
        Map<String, Object> map = new HashMap<>();
        map.put("accountList", accountRepoService.getAccountDTOListWithOutAdmin());
        map.put("roomList", roomRepoService.getAllRoomList());
        return new JsonResponseVO(JsonResponseEnum.MEETING_BEFORE_RES_SUCCESS, map);
    }

    /**
     * 회의 일정 칼릭터 화면
     * @return
     */
    @GetMapping("calendar")
    public ModelAndView getCalendarView() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/meeting/calendar");
        return mv;
    }

    @GetMapping("list")
    public ModelAndView getMeetingList(@ModelAttribute SearchCriteria criteria) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("views/meeting/list");
        mv.addObject("meetingList", meetingDataService.getMeetingAllData4Paging(criteria));
        mv.addObject("pageMaker", meetingDataService.getPageMaker(criteria));
        mv.addObject("nowToday", LocalDateTime.now());
        return mv;
    }

    /**
     * 회의 데이터 추가
     * @param dto
     * @return
     */
    @PostMapping("add/data")
    @ResponseBody
    public JsonResponseVO postAddMeetingData(@RequestBody MeetingDTO dto) {
        return meetingDataService.saveData(dto);
    }


    @PostMapping("update/data")
    @ResponseBody
    public JsonResponseVO postUpdateMeetingData(@RequestBody MeetingDTO dto) {
        return meetingDataService.updateData(dto);
    }

    @PostMapping("delete/data/{idx}")
    @ResponseBody
    public JsonResponseVO postDeleteMeetingData(@PathVariable("idx") Integer idx) {
        return meetingDataService.deleteMeetingData(idx);
    }

    /**
     * 특정 기간에 회의 데이터를 가져오는 메서드
     * @param startStr
     * @param endStr
     * @return
     */
    @GetMapping("get/calendar/{start}/{end}")
    @ResponseBody
    public JsonResponseVO getCalendarStartEnd(@PathVariable(name = "start") String startStr, @PathVariable(name = "end") String endStr ) {
        return meetingDataService.getMeetingData4Calendar(startStr, endStr);
    }

    @GetMapping("/get/data/{idx}")
    @ResponseBody
    public JsonResponseVO getMeetingDataIndex(@PathVariable(name = "idx") Integer index) {
        return meetingDataService.getMeetingData4Index(index);
    }


}
