package com.team.hairdresser.utils.pageablesearch.model;

import java.util.ArrayList;
import java.util.List;

public class PageableSearchFilterDto {

    private PageRequestDto pageRequest;
    private List<SearchCriteriaDto> criteriaList = new ArrayList<>();

    public PageRequestDto getPageRequest() {
        return pageRequest;
    }

    public void setPageRequest(PageRequestDto pageRequest) {
        this.pageRequest = pageRequest;
    }

    public List<SearchCriteriaDto> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<SearchCriteriaDto> criteriaList) {
        this.criteriaList = criteriaList;
    }
}
