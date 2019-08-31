package com.team.hairdresser.utils.pageablesearch.parser;

import java.time.Instant;
import java.util.Date;

public interface DateParser {

    Date parse(String dateString);

    Instant parseAsInstant(String dateString);
}