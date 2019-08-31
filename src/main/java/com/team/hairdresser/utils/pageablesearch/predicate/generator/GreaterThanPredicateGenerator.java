package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class GreaterThanPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public GreaterThanPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        String key = predicateSource.getKey();
        From<?, ?> from = predicateSource.getFrom();

        if (isDateCriteria(from, criteria.getKey())) {
            return builder.greaterThan(from.<Date>get(
                    key), (Date) criteria.getValue());
        } else if (isInstantCriteria(from, criteria.getKey())) {
            return builder.greaterThan(from.<Instant>get(
                    key), (Instant) criteria.getValue());
        } else {
            return builder.greaterThan(from.<String>get(
                    key), criteria.getValue().toString());
        }
    }

}
