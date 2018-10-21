package com.mhlab.br.service.repos;

import com.mhlab.br.domain.dto.AccountDTO;
import com.mhlab.br.jpa.persistence.AccountRepo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private ModelMapper modelMapper;

    @Autowired
    public AccountRepoService(AccountRepo accountRepo, ModelMapper modelMapper) {
        this.accountRepo = accountRepo;
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


}
