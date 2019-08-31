package com.team.hairdresser.utils.pageablesearch.parser;


import com.team.hairdresser.utils.pageablesearch.exception.DateCanNotBeParsedException;

import java.time.Instant;
import java.util.Date;

/**
 * ISO formatted date string parser
 */
public class IsoDateParser implements DateParser {

    @Override
    public Date parse(String dateString) {
        try {
            Instant instant = Instant.parse(dateString);
            return Date.from(instant);
        } catch (Exception e) {
            throw new DateCanNotBeParsedException(dateString, e);
        }
    }

    @Override
    public Instant parseAsInstant(String dateString) {
        try {
            return Instant.parse(dateString);
        } catch (Exception e) {
            throw new DateCanNotBeParsedException(dateString, e);
        }
    }
}

