package com.team.hairdresser.utils.util;


import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

public final class ValidationHelper {

    public static boolean isValid(List list) {
        return list != null && !list.isEmpty();
    }

    public static boolean isValid(Long number) {
        return number != null && number != 0L;
    }

    public static boolean isValid(String str) {
        return str != null && !str.isEmpty();
    }

    /**
     * Validates that the object is not null
     *
     * @param obj object to test
     */
    public static boolean notNull(Object obj) {
        return !(obj == null);
    }

    /**
     * Validates that the array contains any null elements
     *
     * @param objects the array to test
     */
    public static boolean noNullElements(Object[] objects) {
        for (Object obj : objects)
            if (obj == null)
                return false;
        return true;
    }

    /**
     * Validates that the list contains any null elements
     *
     * @param elements the list to test
     */
    public static <T> boolean noNullElements(List<T> elements) {
        for (T obj : elements)
            if (obj == null)
                return false;
        return true;
    }

    /**
     * Validates that the string is not empty
     *
     * @param string the string to test
     */
    public static boolean notEmpty(String string) {
        return !StringUtils.isEmpty(string);
    }

    /**
     * Validates that the string is not empty
     *
     * @param integer the integer to test
     */
    public static boolean notEmpty(Integer integer) {
        return !(integer == null || integer == 0);
    }

    /**
     * Validates that the bigDecimal is not empty
     *
     * @param bigDecimal the bigDecimal to test
     * @return
     */
    public static boolean notEmpty(BigDecimal bigDecimal) {
        return !(bigDecimal == null || bigDecimal == BigDecimal.ZERO);
    }

    /**
     * Validates that the value is not empty
     *
     * @param value the boolean to test
     * @return
     */
    public static boolean notEmpty(Boolean value) {
        return !(value == null || value == false);
    }

    /**
     * Validates that the string is not empty
     *
     * @param value the integer to test
     */
    public static boolean notEmpty(Long value) {
        return !(value == null || value == 0);
    }

}
