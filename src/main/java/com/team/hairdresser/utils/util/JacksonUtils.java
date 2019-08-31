package com.team.hairdresser.utils.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class JacksonUtils {

    private static ObjectMapper ourInstance;
    private static Logger LOGGER = LoggerFactory.getLogger(JacksonUtils.class);

    private JacksonUtils() {
    }

    private static ObjectMapper getInstance() {
        if (ourInstance == null) {
            ourInstance = new ObjectMapper();
        }
        return ourInstance;
    }

    public static <T> T readValue(String value, Class<T> clazz) {
        try {
            return getInstance().readValue(value, clazz);
        } catch (IOException e) {
            LOGGER.error("readValue IOException", e);
        }
        return null;
    }

    public static <T> T readValue(String value, TypeReference<T> typeReference) {
        try {
            return getInstance().readValue(value, typeReference);
        } catch (IOException e) {
            LOGGER.error("readValue IOException", e);
        }
        return null;
    }

    public static String writeValueAsString(Object object) {
        try {
            return getInstance().writeValueAsString(object);
        } catch (IOException e) {
            LOGGER.error("writeValueAsString IOException", e);
        }
        return null;
    }

    public static <T> List<T> readValueToList(String value, Class<T> clazz) {
        try {
            return getInstance().readValue(value, getInstance().getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            LOGGER.error("readValueToList IOException", e);
        }
        return null;
    }


}