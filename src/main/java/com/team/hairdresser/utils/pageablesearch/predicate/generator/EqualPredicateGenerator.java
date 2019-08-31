package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class EqualPredicateGenerator extends AbstractPredicateGenerator {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public EqualPredicateGenerator(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    @Override
    public Predicate toPredicate(SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = getPredicateSourceDto(root, criteria);
        if (criteria.getValue() == null) {
            return builder.isNull(predicateSource.getFrom().get(predicateSource.getKey()));
        } else {
            return builder.equal(predicateSource.getFrom().get(predicateSource.getKey()), criteria.getValue());
        }
    }

}

