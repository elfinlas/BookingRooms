package com.mhlab.br.service.repos;

import com.mhlab.br.jpa.persistence.AccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 계정 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class AccountRepoService {

    private AccountRepo accountRepo;

    @Autowired
    public AccountRepoService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }


    //////////////////////////////
    //          Get
    //////////////////////////////



}
