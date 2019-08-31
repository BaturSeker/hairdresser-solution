package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;

public class NotContainsPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public NotContainsPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        StringBuilder stringBuilder = new StringBuilder("%");
        stringBuilder.append(criteria.getValue());
        stringBuilder.append("%");

        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        return getPredicate(stringBuilder.toString().toLowerCase(), predicateSource.getKey(), predicateSource.getFrom());
    }

    private Predicate getPredicate(String value, String key, From from) {
        return builder.notLike(builder.lower(from.<String>get(
                key)), value);
    }

}

