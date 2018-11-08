package com.mhlab.br.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 회의 참석 인원 처리
 *
 * Created by MHLab on 08/11/2018.
 */


@Getter
@Setter
@Accessors(chain = true)
public class MeetingMemberDTO {

    private int attendIdx;
    private String attendOutMember;
    private AccountDTO attendCompanyMember;

}
