package com.team.hairdresser.utils.pageablesearch.specification;

import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;
import com.team.hairdresser.utils.pageablesearch.predicate.PredicateGeneratorFactory;
import com.team.hairdresser.utils.pageablesearch.predicate.generator.AbstractPredicateGenerator;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class CustomSearchSpecification<T> implements Specification<T> {

    private SearchCriteriaDto criteria;

    public CustomSearchSpecification(SearchCriteriaDto criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate
            (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        PredicateGeneratorFactory predicateGeneratorFactory = new PredicateGeneratorFactory(root, query, builder);
        AbstractPredicateGenerator predicateGenerator = predicateGeneratorFactory.getPredicateGenerator(criteria);
        query.distinct(true);
        return predicateGenerator.toPredicate(criteria);
    }

    public boolean isOr() {
        return this.criteria.getOr();
    }
}
