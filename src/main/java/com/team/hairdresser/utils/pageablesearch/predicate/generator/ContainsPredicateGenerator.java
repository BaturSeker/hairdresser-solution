package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;

public class ContainsPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public ContainsPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        StringBuilder stringBuilder = new StringBuilder("%");
        Object value = criteria.getValue();
        if (value == null) {
            value = "";
        }
        stringBuilder.append(value);
        stringBuilder.append("%");

        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        return getPredicate(stringBuilder.toString().toLowerCase(), predicateSource.getKey(), predicateSource.getFrom());
    }

    private Predicate getPredicate(String value, String key, From from) {
        return builder.like(builder.lower(from.<String>get(
                key)), value);
    }

}

