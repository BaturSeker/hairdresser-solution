package com.team.hairdresser.config.mapper;

import java.sql.Timestamp;
import java.time.Instant;

public class TimestampMapper {

    public String asString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = timestamp.toInstant();
        return instant.toString();
    }

    public Timestamp asTimestamp(String timestamp) {
        if (timestamp == null) {
            return null;
        }
        Instant instant = Instant.parse(timestamp);
        return Timestamp.from(instant);
    }
}
