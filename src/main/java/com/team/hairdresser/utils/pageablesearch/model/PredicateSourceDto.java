package com.team.hairdresser.utils.pageablesearch.model;

import javax.persistence.criteria.From;

public class PredicateSourceDto {

    private From from;
    private String key;

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
