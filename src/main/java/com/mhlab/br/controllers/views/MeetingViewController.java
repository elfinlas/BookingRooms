package com.mhlab.br.controllers.views;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.domain.enums.JsonResponseEnum;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.service.data.MeetingDataService;
import com.mhlab.br.service.data.RoomDataService;
import com.mhlab.br.service.repos.AccountRepoService;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
public class MeetingViewController {

    private AccountRepoService accountRepoService;
    private RoomRepoService roomRepoService;
    private MeetingDataService meetingDataService;


    public MeetingViewController(AccountRepoService accountRepoService, RoomRepoService roomRepoService, MeetingDataService meetingDataService) {
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


    @PostMapping("add/data")
    @ResponseBody
    public JsonResponseVO postAddMeetingData(@RequestBody MeetingDTO dto) {
        log.info("dto = "+ dto);
        return meetingDataService.saveData(dto);
    }

}
