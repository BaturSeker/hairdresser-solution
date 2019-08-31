package com.team.hairdresser.utils.pageablesearch.predicate.generator;


import com.team.hairdresser.utils.pageablesearch.model.PredicateSourceDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;

public abstract class AbstractPredicateGenerator {

    private static final String DOT = ".";
    public static final String ENTITY = "Entity";
    public static final String ID_CAPITALIZED = "Id";
    public static final String BASE_ENTITY_ID_LOWERCASE = "id";

    protected boolean isDateCriteria(From root, String key) {
        return Date.class.isAssignableFrom(getJavaTypeOfKeyField(root, key));
    }

    protected boolean isInstantCriteria(From root, String key) {
        return Instant.class.isAssignableFrom(getJavaTypeOfKeyField(root, key));
    }

    private Class getJavaTypeOfKeyField(From root, String key) {
        return root.get(key).getJavaType();
    }

    public abstract Predicate toPredicate(SearchCriteriaDto criteria);

    protected PredicateSourceDto getPredicateSourceDto(Root root, SearchCriteriaDto criteria) {
        PredicateSourceDto predicateSource = new PredicateSourceDto();
        predicateSource.setFrom(root);
        predicateSource.setKey(criteria.getKey());

        return joinNestedFields(predicateSource);
    }

    private PredicateSourceDto joinNestedFields(PredicateSourceDto predicateSource) {
        String key = predicateSource.getKey();
        if (key.contains(DOT)) {
            String parentFieldKey = key.substring(0, key.indexOf(DOT));
            String nestedFieldKey = key.substring(key.indexOf(DOT) + 1);
            predicateSource.setKey(nestedFieldKey);
            predicateSource.setFrom(predicateSource.getFrom().join(parentFieldKey));

            return joinNestedFields(predicateSource);
        }
        if (key.endsWith(ID_CAPITALIZED)) {
            updatePredicateSourceKeyIfBaseEntityIdNeeded(predicateSource);
        }
        return predicateSource;
    }

    private void updatePredicateSourceKeyIfBaseEntityIdNeeded(PredicateSourceDto predicateSource) {
        String key = predicateSource.getKey();
        From from = predicateSource.getFrom();
        Class entityClass = from.getJavaType();
        Field field = ReflectionUtils.findField(entityClass, predicateSource.getKey());
        if (field == null) {
            String uncapitalizedEntityName = getUncapitalizedEntityName(entityClass);
            if (key.equals(uncapitalizedEntityName + ID_CAPITALIZED)) {
                updatePredicateSourceKeyIfFieldNotFound(predicateSource, entityClass);
            }
        }
    }

    private String getUncapitalizedEntityName(Class entityClass) {
        String simpleClassName = entityClass.getSimpleName();
        if (simpleClassName.endsWith(ENTITY)) {
            simpleClassName = simpleClassName.substring(0, simpleClassName.length() - ENTITY.length());
        }
        return StringUtils.uncapitalize(simpleClassName);
    }

    private void updatePredicateSourceKeyIfFieldNotFound(PredicateSourceDto predicateSource, Class entityClass) {
        Field field = ReflectionUtils.findField(entityClass, BASE_ENTITY_ID_LOWERCASE);
        if (field != null) {
            predicateSource.setKey(BASE_ENTITY_ID_LOWERCASE);
        }
    }

}
