package com.mhlab.br.service.data;

import com.mhlab.br.domain.dto.MeetingDTO;
import com.mhlab.br.domain.vo.JsonResponseVO;
import com.mhlab.br.service.repos.RoomRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 회의실 데이터를 처리하는 서비스 객체
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class RoomDataService {

    private RoomRepoService roomRepoService;

    public RoomDataService(RoomRepoService roomRepoService) {
        this.roomRepoService = roomRepoService;
    }



    public boolean saveData(MeetingDTO dto) {



        return true;
    }
}
