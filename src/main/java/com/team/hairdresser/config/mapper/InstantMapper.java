package com.team.hairdresser.config.mapper;

import java.time.Instant;

public class InstantMapper {

    public String asString(Instant instant) {
        return instant != null ? instant.toString() : null;
    }

    public Instant asInstant(String instant) {
        return instant != null ? Instant.parse(instant) : null;
    }
}

