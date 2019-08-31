package com.team.hairdresser.utils.util.string;

public final class StringAppenderUtil {

    private StringAppenderUtil() {
    }

    public static String append(Object... objects) {
        if (objects == null || objects.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (Object object : objects) {
            builder.append(object.toString());
        }

        return builder.toString();
    }
}