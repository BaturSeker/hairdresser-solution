package com.team.hairdresser.utils.pageablesearch.exception;

/**
 * Exceptin to be thrown while parsing date values in SearchCriteria
 */
public class DateCanNotBeParsedException extends RuntimeException {
    public DateCanNotBeParsedException(String dateString, Exception e) {
        super("Date string can not be parsed: " + dateString, e);
    }
}