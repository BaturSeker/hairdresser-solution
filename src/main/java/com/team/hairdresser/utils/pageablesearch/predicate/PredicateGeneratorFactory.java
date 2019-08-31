package com.team.hairdresser.utils.pageablesearch.predicate;


import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchOperationEnum;
import com.team.hairdresser.utils.pageablesearch.predicate.generator.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PredicateGeneratorFactory {

    private Root<?> root;
    private CriteriaQuery<?> query;
    private CriteriaBuilder builder;

    public PredicateGeneratorFactory(Root<?> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        this.root = root;
        this.query = query;
        this.builder = builder;
    }

    public AbstractPredicateGenerator getPredicateGenerator(SearchCriteriaDto criteria) {
        SearchOperationEnum operation = SearchOperationEnum.getOperation(criteria.getOperation());
        if (operation == null) {
            return null;
        }
        switch (operation) {
            case EQUALITY:
                return new EqualPredicateGenerator(root, query, builder);
            case NEGATION:
                return new NotEqualPredicateGenerator(root, query, builder);
            case GREATER_THAN:
                return new GreaterThanPredicateGenerator(root, query, builder);
            case GREATER_THAN_OR_EQUAL:
                return new GreaterThanOrEqualPredicateGenerator(root, query, builder);
            case LESS_THAN:
                return new LessThanPredicateGenerator(root, query, builder);
            case LESS_THAN_OR_EQUAL:
                return new LessThanOrEqualPredicateGenerator(root, query, builder);
            case CONTAINS:
                return new ContainsPredicateGenerator(root, query, builder);
            case BETWEEN:
                return new BetweenPredicateGenerator(root, query, builder);
            case NOT_CONTAINS:
                return new NotContainsPredicateGenerator(root, query, builder);
            default:
                return null;
        }
    }
}