package com.mhlab.br.domain.pages;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * 페이징을 사용하는 게시판에서 페이징 요소를 만들고 제공하는 클래스ㄴ
 * Created by MHLab on 2018. 10. 17...
 */


@Slf4j
@Getter
public class PageMaker {
    //외부 주입 필요
    private int totalCount; //총 데이터의 개수
    private Criteria criteria; //페이지 정보를 담고 있는 Criteria 객체

    //내부 연산
    private int startPage; //Paging의 시작 페이지 숫자
    private int endPage; //Paging의 끝 페이지 숫자
    private boolean prev; //이전 버튼의 활성화 유무
    private boolean next; //다음 버튼의 활성화 유무


    private int pagingCnt = 10; //하단 페이지 숫자 개수 (10개)


    public PageMaker(int totalCount, Criteria criteria) {
        this.totalCount = totalCount;
        this.criteria = criteria;

        //마지막 페이지 구하는 식
        this.endPage  = (int) (Math.ceil((criteria.getPage()==0?1:criteria.getPage()) / (double)pagingCnt) * pagingCnt); //마지막 페이지 구하는 식
        this.startPage = (endPage - pagingCnt) + 1; //시작 페이지

//        this.endPage  = (int) (Math.ceil(criteria.getPage() / (double)pagingCnt) * pagingCnt); //마지막 페이지 구하는 식
//        this.startPage = (endPage - pagingCnt) + 1; //시작 페이지

        int tmpLastPageNum = (int) (Math.ceil(this.totalCount / (double) criteria.getPerPageNum())); //마지막 페이지 숫자를 구함

        if(endPage>tmpLastPageNum) { endPage = tmpLastPageNum; } //마지막 페이지가 기존에 구한 endPage보다 클 경우 재 산정

        this.prev = this.startPage != 1; //1페이지 일 경우 false 로 처리
        this.next = this.endPage * criteria.getPerPageNum() < totalCount; //마지막 페이지가 최종 값과 비슷하거나 클 경우 false 처리

//        this.prev = this.startPage == 1 ? false : true; //1페이지 일 경우 false 로 처리
//        this.next = this.endPage * criteria.getPerPageNum() >= totalCount ? false : true; //마지막 페이지가 최종 값과 비슷하거나 클 경우 false 처리
    }



    /**
     *
     * @param nowPageNum
     * @return
     */
    public String makeUrl4Page(int nowPageNum) {
        return getDefaultUriComponents(nowPageNum).build().toUriString();
    }

    /**
     * 일반적인 리스트에서 사용하는 makeUrl 메서드
     * @param nowPageNum
     * @return
     */
    public String makeUrl4SearchPage(int nowPageNum) {
        return getDefaultUriComponents(nowPageNum)
                .queryParam("keyword", ((SearchCriteria)criteria).getKeyword())
                .queryParam("searchWord", ((SearchCriteria)criteria).getSearchWord())
                .build().toUriString();
    }


    /**
     * 열람 페이지 등에서 사용하는 리스트 url maker
     * @param nowPageNum
     * @param readParam 열람 파라메터 (보통 no 사용)
     * @param num 열람 파라메터 인덱스
     * @return
     */
    public String makeUrl4ReadSearchPage(int nowPageNum, String readParam, int num) {
        return getDefaultUriComponents(nowPageNum)
                .queryParam(readParam, num)
                .queryParam("keyword", ((SearchCriteria)criteria).getKeyword())
                .queryParam("searchWord", ((SearchCriteria)criteria).getSearchWord())
                .build().toUriString();
    }


    /**
     *
     * @param nowPageNum
     * @return
     */
    private UriComponentsBuilder getDefaultUriComponents(int nowPageNum) {
        return UriComponentsBuilder.newInstance()
                .queryParam("page", nowPageNum)
                .queryParam("perPageNum", this.criteria.getPerPageNum());
    }


}
