package com.mhlab.br.service.repos;

import com.mhlab.br.domain.dto.AccountDTO;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.AutoLogin;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.jpa.persistence.AutoLoginRepo;
import com.mhlab.br.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 계정 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class AccountRepoService {

    private AccountRepo accountRepo;
    private AutoLoginRepo autoLoginRepo;
    private ModelMapper modelMapper;

    @Autowired
    public AccountRepoService(AccountRepo accountRepo, AutoLoginRepo autoLoginRepo, ModelMapper modelMapper) {
        this.accountRepo = accountRepo;
        this.autoLoginRepo = autoLoginRepo;
        this.modelMapper = modelMapper;
    }


    //////////////////////////////
    //          Get
    //////////////////////////////

    /**
     * 계정 중 관리자를 제외한 DTO 데이터를 가져오는 메서드
     * @return
     */
    public List<AccountDTO> getAccountDTOListWithOutAdmin() {
        return accountRepo.findByIdIsNot("admin")
                .stream().map(account -> modelMapper.map(account, AccountDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Account id로 계정 정보를 가져오는 메서드
     * @param accountId
     * @return
     */
    public Account getAccountData4Id(String accountId) {
        return accountRepo.findById(accountId);
    }



    //////////////////////////////
    //      Check Data
    //////////////////////////////

    /**
     * 사용자 ID와 PW(비 암호화)를 체크하는 메서드
     * @param accountId
     * @param normalPw (평문)
     * @return
     */
    public boolean checkIdAndNormalPw(String accountId, String normalPw) {
        return accountRepo.findByIdAndPw(accountId, SecurityUtils.encryptData4SHA(normalPw)).isPresent();
    }



    ///////////////////////////////////
    //  Save And Update Data
    ///////////////////////////////////

    public void saveAccountData(Account account) {
        LocalDateTime now = LocalDateTime.now();
        account.setCreateDate(now)
                .setUpdateDate(now);
        accountRepo.save(account);
    }

    /**
     * AutoLogin Data를 저장해주는 메서드
     * @param autoLogin
     */
    public void saveAutoLoginData(AutoLogin autoLogin) {
        autoLogin.setCreateDate(LocalDateTime.now());
        autoLoginRepo.save(autoLogin);
    }



}
