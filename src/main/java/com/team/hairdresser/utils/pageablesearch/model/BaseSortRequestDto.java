package com.team.hairdresser.utils.pageablesearch.model;

import org.springframework.data.domain.Sort;

public class BaseSortRequestDto {
    private String dir;
    private String field;
    private Sort.NullHandling nullHandling;

    public BaseSortRequestDto(String dir, String field) {
        this.dir = dir;
        this.field = field;
    }

    public BaseSortRequestDto(String dir, String field, Sort.NullHandling nullHandling) {
        this.dir = dir;
        this.field = field;
        this.nullHandling = nullHandling;
    }

    public BaseSortRequestDto() {
    }

    public Sort.NullHandling getNullHandling() {
        return nullHandling;
    }

    public void setNullHandling(Sort.NullHandling nullHandling) {
        this.nullHandling = nullHandling;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDir() {
        return dir;
    }

    public String getField() {
        return field;
    }
}

