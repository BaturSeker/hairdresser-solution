package com.team.hairdresser.utils.pageablesearch.criteria;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class NumberParseUtil {
    private static Map<Class, Class<? extends Number>> primitiveNumberTypes = new HashMap<>();

    static {
        Arrays.asList(short.class, int.class, long.class, float.class, double.class);
        primitiveNumberTypes.put(short.class, Short.class);
        primitiveNumberTypes.put(int.class, Integer.class);
        primitiveNumberTypes.put(long.class, Long.class);
        primitiveNumberTypes.put(float.class, Float.class);
        primitiveNumberTypes.put(double.class, Double.class);
    }

    private NumberParseUtil() {
    }

    public static Number asNumber(String str,
                                  Class<? extends Number> param) throws UnsupportedOperationException {
        if (str == null || str.isEmpty()) {
            return null;
        }

        try {
            if (primitiveNumberTypes.get(param) != null) {
                param = primitiveNumberTypes.get(param);
            }

            /*
             * Try to access the staticFactory method for:
             * Byte, Short, Integer, Long, Double, and Float
             */
            Method m = param.getMethod("valueOf", String.class);
            Object o = m.invoke(param, str);
            return param.cast(o);
        } catch (NoSuchMethodException e1) {
            /* Try to access the constructor for BigDecimal or BigInteger*/
            try {
                Constructor<? extends Number> ctor = param
                        .getConstructor(String.class);
                return ctor.newInstance(str);
            } catch (ReflectiveOperationException e2) {
                /* AtomicInteger and AtomicLong not supported */
                throw new UnsupportedOperationException(
                        "Cannot convert string to " + param.getName());
            }
        } catch (ReflectiveOperationException e2) {
            throw new UnsupportedOperationException("Cannot convert string to "
                    + param.getName());
        }
    }
}

