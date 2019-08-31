package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class LessThanOrEqualPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public LessThanOrEqualPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        String key = predicateSource.getKey();
        From<?, ?> from = predicateSource.getFrom();

        if (isDateCriteria(from, key)) {
            return builder.lessThanOrEqualTo(from.<Date>get(
                    key), (Date) criteria.getValue());
        } else if (isInstantCriteria(from, criteria.getKey())) {
            return builder.lessThanOrEqualTo(from.<Instant>get(
                    key), (Instant) criteria.getValue());
        } else {
            return builder.lessThanOrEqualTo(from.<String>get(
                    key), criteria.getValue().toString());
        }
    }

}
