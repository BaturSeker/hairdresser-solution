package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.*;
import java.time.Instant;
import java.util.Date;

public class GreaterThanOrEqualPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public GreaterThanOrEqualPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        return getPredicate(criteria, predicateSource.getKey(), predicateSource.getFrom());
    }

    private Predicate getPredicate(SearchCriteriaDto criteria, String key, From<?, ?> from) {
        if (isDateCriteria(from, key)) {
            return builder.greaterThanOrEqualTo(from.<Date>get(
                    key), (Date) criteria.getValue());
        } else if (isInstantCriteria(from, criteria.getKey())) {
            return builder.greaterThanOrEqualTo(from.<Instant>get(
                    key), (Instant) criteria.getValue());
        } else {
            return builder.greaterThanOrEqualTo(from.<String>get(
                    key), criteria.getValue().toString());
        }
    }

}
