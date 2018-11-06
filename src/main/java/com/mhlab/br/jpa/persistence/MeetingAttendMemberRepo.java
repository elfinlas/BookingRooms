package com.mhlab.br.jpa.persistence;

import com.mhlab.br.jpa.entity.MeetingMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 회의 참석자를 저장하는 리포지토리 객체
 *
 * Created by MHLab on 05/11/2018..
 */

@Repository
public interface MeetingAttendMemberRepo extends JpaRepository<MeetingMember, Integer> {


}
