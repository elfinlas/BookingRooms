package com.mhlab.br.service.abstracts;

import com.mhlab.br.domain.pages.Criteria;
import com.mhlab.br.domain.pages.PageMaker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/**
 * 페이징 데이터를 처리할 때 사용하는 추상 클래스
 *
 * Created by MHLab on 01/11/2018.
 */


public abstract class AbstractListPageService {

    /**
     *
     * @param page
     * @param perPageCnt
     * @param sort
     * @param properties
     * @return
     */
    protected PageRequest getPageRequest(int page, int perPageCnt, Sort.Direction sort, String properties) {
        return PageRequest.of(page, perPageCnt, sort, properties);
    }


    /**
     *
     * @param totalCount
     * @param criteria
     * @return
     */
    protected PageMaker getPageMaker(int totalCount, Criteria criteria) {
        return new PageMaker(totalCount==0?1:totalCount, criteria);
    }
}
