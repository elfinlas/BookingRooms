package com.mhlab.br.service.repos;

import com.mhlab.br.domain.dto.AccountDTO;
import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.domain.pages.PageMaker;
import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.AutoLogin;
import com.mhlab.br.jpa.persistence.AccountRepo;
import com.mhlab.br.jpa.persistence.AutoLoginRepo;
import com.mhlab.br.service.abstracts.AbstractListPageService;
import com.mhlab.br.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 계정 데이터를 Repo로 부터 CRUD 담당을 처리하는 서비스
 *
 * Created by MHLab on 19/10/2018.
 */

@Slf4j
@Service
public class AccountRepoService extends AbstractListPageService {

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

    /**
     * index로 계정 정보를 가져오는 메서드
     * @param accountIdx
     * @return
     */
    public Account getAccountData4Idx(int accountIdx) { return accountRepo.getOne(accountIdx); }

    /**
     * 자동 로그인 토큰으로 데이터를 찾는 메서드
     * @param token
     * @return
     */
    public Optional<AutoLogin> getAutoLoginData4Token(String token) {
        return autoLoginRepo.findByToken(token);
    }


    /**
     * 전체 사용자 리스트를 페이징 해서 가져오는 메서드
     * @param criteria
     * @return
     */
    public List<Account> getAllAccountPageList(Criteria criteria) {
        return accountRepo.findAllBy(PageRequest.of(criteria.getPage(), criteria.getPerPageNum(), Sort.Direction.DESC, "createDate")).stream()
                .filter(account -> !account.getId().equals("admin"))
                .collect(Collectors.toList());
    }

    /**
     * Account 용 PageMaker를 만들어주는 메서드
     * @param criteria
     * @return
     */
    public PageMaker getPageMaker(Criteria criteria) {
        return getPageMaker(accountRepo.countAllBy(), criteria);
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
     * 계정 정보 업데이트
     * @param account
     */
    public void updateAccountData(Account account) {
        account.setUpdateDate(LocalDateTime.now());
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


    ///////////////////////////////////
    //        Delete Data
    ///////////////////////////////////

    /**
     * 전달된 데이터를 삭제하는 메서드
     * @param targetAutoLogin
     */
    public void deleteAutoLoginData(AutoLogin targetAutoLogin) {
        autoLoginRepo.delete(targetAutoLogin);
    }


    /**
     * 계정 삭제
     * @param accountIdx
     */
    public void deleteAccount(int accountIdx) {
        accountRepo.delete(accountRepo.getOne(accountIdx));
    }

}
