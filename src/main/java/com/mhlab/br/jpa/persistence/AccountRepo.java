package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Account Entity를 처리하는 리포지토리 객체
 *
 * Created by MHLab on 18/10/2018.
 */
@Repository
public interface AccountRepo extends JpaRepository<Account, Integer> {


    /**
     * 로그인 처리를 위해 사용하는 메서드
     * @param id
     * @param pw
     * @return
     */
    Account findByIdAndPw(String id, String pw);



    /////////////////////////
    //    WithOut Paging
    /////////////////////////




    /////////////////////////
    //     With Paging
    /////////////////////////

}
