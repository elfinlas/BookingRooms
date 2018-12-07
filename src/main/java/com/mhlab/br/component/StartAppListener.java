package com.mhlab.br.component;

import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.Room;
import com.mhlab.br.service.repos.AccountRepoService;
import com.mhlab.br.service.repos.RoomRepoService;
import com.mhlab.br.utils.SecurityUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 어플리케이션 시작 시 이벤트
 *
 * Created by MHLab on 08/12/2018..
 */

@Component
public class StartAppListener implements ApplicationListener<ContextRefreshedEvent> {

    private AccountRepoService accountRepoService;
    private RoomRepoService roomRepoService;

    public StartAppListener(AccountRepoService accountRepoService, RoomRepoService roomRepoService) {
        this.accountRepoService = accountRepoService;
        this.roomRepoService = roomRepoService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        //처음 수행하는 경우
        if (accountRepoService.getAccountData4Id("admin") == null) {
            Account admin = new Account()
                    .setId("admin")
                    .setPw(SecurityUtils.encryptData4SHA("admin"))
                    .setName("admin")
                    .setTeamName("admin");
            accountRepoService.saveAccountData(admin);

            Room defaultRoom = new Room()
                    .setName("기본 회의실")
                    .setDescription("Default Room");
            roomRepoService.saveRoomData(defaultRoom);
        }
    }
}
