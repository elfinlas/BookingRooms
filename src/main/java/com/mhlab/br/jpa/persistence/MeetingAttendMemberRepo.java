package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.Account;
import com.mhlab.br.jpa.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 회의 참석자를 저장하는 리포지토리 객체
 *
 * Created by MHLab on 05/11/2018..
 */

@Repository
public interface MeetingAttendMemberRepo extends JpaRepository<MeetingMember, Integer> {


    /**
     * 참석자 중 사내 사용자를 검색하는 메서드
     * @param account
     * @return
     */
    List<MeetingMember> findByAttendCompanyMember(Account account);

}
