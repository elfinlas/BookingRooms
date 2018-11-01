package com.mhlab.br.domain.pages;

import lombok.Getter;

/**
 * 페이징에서 사용할 객체
 * 현재 페이지의 번호와 페이지 당 보여질 데이터 갯수를 받는다.
 *
 * Created by MHLab on 2018. 10. 17...
 */

@Getter
public class Criteria {
    private int page; //현재 페이지 번호
    private int perPageNum; //페이지 당 보여질 데이터 수

    /**
     * 기본 생성자이며, 기본 값을 설정해준다.
     */
    public Criteria() {
        this.page = 1;
        this.perPageNum = 10;
    }

    /**
     * 페이지의 값이 0이거나 이하인 경우엔 1로 설정
     * @param page
     */
    public void setPage(int page) {
        this.page = page <=0 ? 1:page;
    }

    public void setPerPageNum(int perPageNum) {
        this.perPageNum = perPageNum;
    }


    @Override
    public String toString() {
        return "Criteria [page = " + page + ", perPAgeNum = " + perPageNum + "]";
    }
}
