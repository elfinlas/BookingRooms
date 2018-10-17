package com.mhlab.BookingRooms.domain.pages;

import lombok.Getter;
import lombok.Setter;

/**
 * Criterial를 확장한 클래스 (검색 시 사용)
 *
 * Created by MHLab on 2018. 10. 17...
 */


@Getter
@Setter
public class SearchCriteria extends Criteria {
    private String keyword;
    private String searchWord;


    public SearchCriteria() {
        this.keyword = "a";
        this.searchWord = "";
    }

    @Override
    public String toString() {
        return super.toString() + " SearchCriteria [keyword = " + this.keyword + ", searchWord = " + this.searchWord + "]";
    }
}
