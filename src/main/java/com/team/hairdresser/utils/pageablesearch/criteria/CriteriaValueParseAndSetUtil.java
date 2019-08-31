package com.team.hairdresser.utils.pageablesearch.criteria;


import com.team.hairdresser.utils.pageablesearch.model.SearchCriteriaDto;
import com.team.hairdresser.utils.pageablesearch.model.SearchFieldTypeEnum;
import com.team.hairdresser.utils.pageablesearch.parser.DateParser;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public final class CriteriaValueParseAndSetUtil {

    private static final List<? extends Class<? extends Number>> primitiveNumberTypes = Arrays.asList(short.class, int.class, long.class, float.class, double.class);

    private CriteriaValueParseAndSetUtil() {
    }

    public static void parseAndSetCriteriaValues(SearchCriteriaDto criteria, Class<?> type, DateParser dateParser) {

        if (String.class.isAssignableFrom(type)) {
            criteria.setFieldType(SearchFieldTypeEnum.STRING);
        } else if (Date.class.isAssignableFrom(type)) {
            criteria.setFieldType(SearchFieldTypeEnum.DATE);
            parseAndSetDateValues(criteria, dateParser);
        } else if (Instant.class.isAssignableFrom(type)) {
            criteria.setFieldType(SearchFieldTypeEnum.INSTANT);
            parseAndSetInstantValues(criteria, dateParser);
        } else if (Number.class.isAssignableFrom(type) || isPrimitiveNumber(type)) {
            criteria.setFieldType(SearchFieldTypeEnum.NUMBER);
            parseAndSetNumberValues(criteria, type);
        } else if (Enum.class.isAssignableFrom(type)) {
            criteria.setFieldType(SearchFieldTypeEnum.ENUM);
            parseAndSetEnumValues(criteria, type);
        } else if (Boolean.class.isAssignableFrom(type)) {
            criteria.setFieldType(SearchFieldTypeEnum.BOOLEAN);
            parseAndSetBooleanValues(criteria);
        }

    }


    private static void parseAndSetInstantValues(SearchCriteriaDto criteria, DateParser dateParser) {
        if (criteria.getValue() instanceof String) {
            criteria.setValue(dateParser.parseAsInstant((String) criteria.getValue()));
        }
        if (criteria.getEndValue() instanceof String) {
            criteria.setEndValue(dateParser.parseAsInstant((String) criteria.getEndValue()));
        }
    }

    private static void parseAndSetDateValues(SearchCriteriaDto criteria, DateParser dateParser) {
        if (criteria.getValue() instanceof String) {
            criteria.setValue(dateParser.parse((String) criteria.getValue()));
        }
        if (criteria.getEndValue() instanceof String) {
            criteria.setEndValue(dateParser.parse((String) criteria.getEndValue()));
        }
    }

    private static void parseAndSetBooleanValues(SearchCriteriaDto criteria) {
        if (criteria.getValue() instanceof String) {
            if (criteria.getValue().equals("1")) {
                criteria.setValue(Boolean.TRUE);
            } else if (criteria.getValue().equals("0")) {
                criteria.setValue(Boolean.FALSE);
            } else {
                criteria.setValue(Boolean.valueOf((String) criteria.getValue()));
            }
        }
    }

    private static void parseAndSetEnumValues(SearchCriteriaDto criteria, Class<?> type) {
        if (criteria.getValue() instanceof String) {

            Enum enumConstantIfMatches = getEnumConstantIfMatches((String) criteria.getValue(), type);
            if (enumConstantIfMatches != null) {
                criteria.setValue(enumConstantIfMatches);
            } else {
                criteria.setValue(NumberParseUtil.asNumber((String) criteria.getValue(), Integer.class));
            }
        }

        if (criteria.getEndValue() != null && criteria.getEndValue() instanceof String) {

            Enum enumConstantIfMatches = getEnumConstantIfMatches((String) criteria.getEndValue(), type);
            if (enumConstantIfMatches != null) {
                criteria.setEndValue(enumConstantIfMatches);
            } else {
                criteria.setEndValue(NumberParseUtil.asNumber((String) criteria.getEndValue(), Integer.class));
            }
        }
    }

    private static Enum getEnumConstantIfMatches(String text, Class<?> type) {
        Enum[] enumConstants = (Enum[]) type.getEnumConstants();
        for (Enum candidate : enumConstants) {
            if (candidate.name().equalsIgnoreCase(text)) {
                return candidate;
            }
        }
        return null;
    }

    private static void parseAndSetNumberValues(SearchCriteriaDto criteria, Class<?> type) {
        if (criteria.getValue() instanceof String) {
            Number value = NumberParseUtil.asNumber((String) criteria.getValue(), (Class<? extends Number>) type);
            criteria.setValue(value);
        }
        if (criteria.getEndValue() != null && criteria.getEndValue() instanceof String) {
            Number endNumber = NumberParseUtil.asNumber((String) criteria.getEndValue(), (Class<? extends Number>) type);
            criteria.setEndValue(endNumber);
        }
    }

    private static boolean isPrimitiveNumber(Class<?> type) {
        for (Class primitiveNumberType : primitiveNumberTypes) {
            if (primitiveNumberType.isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

}

