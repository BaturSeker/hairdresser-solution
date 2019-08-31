package com.team.hairdresser.utils.pageablesearch.criteria;


import com.team.hairdresser.utils.abstracts.BaseEntity;
import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;
import com.team.hairdresser.utils.pageablesearch.parser.DateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public final class CriteriaKeyFieldTypeDeterminerUtil {

    private static final Logger log = LoggerFactory.getLogger(CriteriaKeyFieldTypeDeterminerUtil.class);

    public static final String DOT = ".";
    public static final String ENTITY = "Entity";
    public static final String ID_CAPITALIZED = "Id";
    public static final String BASE_ENTITY_ID_LOWERCASE = "id";

    public static void setFieldTypes(List<SearchCriteriaDto> criteriaList, Class<?> entityClass, DateParser dateParser) {
        for (SearchCriteriaDto criteria : criteriaList) {

            try {

                Field keyField = getDeclaredField(entityClass, criteria.getKey());
                Class<?> type = keyField.getType();

                if (Object.class.equals(type)) {
                    type = castObjectToAssignable(entityClass);
                }

                CriteriaValueParseAndSetUtil.parseAndSetCriteriaValues(criteria, type, dateParser);

            } catch (NoSuchFieldException e) {
                log.error("NoSuchFieldException in " + entityClass.getName());
            }
        }
    }

    private static Class<?> castObjectToAssignable(Class<?> entityClass) {
        if (entityClass.getSuperclass().equals(BaseEntity.class)) {
            Type genericSuperclass = entityClass.getGenericSuperclass();

            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericSuperclass;
                return (Class) pt.getActualTypeArguments()[0];
            }
        }

        return Object.class;
    }

    private static Field getDeclaredField(Class<?> entityClass, String key) throws NoSuchFieldException {
        if (key.contains(DOT)) {
            String parentFieldKey = key.substring(0, key.indexOf(DOT));
            Field declaredField = entityClass.getDeclaredField(parentFieldKey);
            Class<?> nestedTypeClass = getNestedTypeClass(declaredField);

            String nestedFieldKey = key.substring(key.indexOf(DOT) + 1);
            return getDeclaredField(nestedTypeClass, nestedFieldKey);
        } else {
            Field field = ReflectionUtils.findField(entityClass, key);
            if (field == null) {
                field = getBaseEntityIdFieldIfNeeded(entityClass, key);
            }
            return field;
        }
    }

    private static Class<?> getNestedTypeClass(Field declaredField) {
        Class<?> nestedTypeClass = declaredField.getType();

        if (Collection.class.isAssignableFrom(nestedTypeClass)) {
            Type type = declaredField.getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                for (Type t : pt.getActualTypeArguments()) {
                    nestedTypeClass = (Class<?>) t;
                }
            }
        }
        return nestedTypeClass;
    }

    private static Field getBaseEntityIdFieldIfNeeded(Class<?> entityClass, String key) {
        Field field = null;
        String simpleClassName = entityClass.getSimpleName();
        if (simpleClassName.endsWith(ENTITY)) {
            simpleClassName = simpleClassName.substring(0, simpleClassName.length() - ENTITY.length());
        }
        simpleClassName = StringUtils.uncapitalize(simpleClassName);
        if (key.equals(simpleClassName + ID_CAPITALIZED)) {
            field = ReflectionUtils.findField(entityClass, BASE_ENTITY_ID_LOWERCASE);
        }

        return field;
    }

}
