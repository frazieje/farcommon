package com.spoohapps.farcommon.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ConfigMethod<T> {

    private final Set<String> flags = ConcurrentHashMap.newKeySet();

    private String valueString;

    private final Method method;

    static <T> ConfigMethod<T> parseAnnotations(Method method) {
        return new ConfigMethod<>(method);
    }

    @SuppressWarnings("unchecked")
    private ConfigMethod(Method method) {

        if (!method.isAnnotationPresent(ConfigFlags.class)) {
            throw new IllegalArgumentException("Config methods must define flags");
        }

        Type returnType = method.getGenericReturnType();
        if (returnType == void.class) {
            throw new IllegalArgumentException("Config methods must not return void");
        }
        if (method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Config methods must not define formal parameters");
        }

        this.method = method;

        Arrays.stream(method.getAnnotations())
                .forEach(this::parseMethodAnnotation);

    }

    private void parseMethodAnnotation(Annotation annotation) {
        if (annotation instanceof ConfigFlags) {
            Arrays.stream(((ConfigFlags) annotation).value())
                    .forEach(this::addFlag);
        }
    }

    private void addFlag(String flag) {
        if (flag.contains("-")) {
            throw new IllegalArgumentException("Config flags must not contain '-' character");
        }
        flags.add(flag);
    }

    @SuppressWarnings("unchecked")
    T invoke(Object[] args) {

        boolean isPrimitive = isPrimitive(method.getReturnType());

        Class<?> valueType = boxIfPrimitive(method.getReturnType());

        if (valueType.isAssignableFrom(Integer.class)) {
            try {
                return (T) Integer.valueOf(valueString);
            } catch (Exception nfe) {
                if (!isPrimitive) return null;
                return (T) Integer.valueOf(0);
            }
        }
        if (valueType.isAssignableFrom(Long.class)) {
            try {
                return (T) Long.valueOf(valueString);
            } catch (Exception nfe) {
                if (!isPrimitive) return null;
                return (T) Long.valueOf(0);
            }
        }
        if (valueType.isAssignableFrom(Short.class)) {
            try {
                return (T) Short.valueOf(valueString);
            } catch (Exception nfe) {
                if (!isPrimitive) return null;
                return (T) Short.valueOf((short)0);
            }
        }
        if (valueType.isAssignableFrom(Byte.class)) {
            try {
                return (T) Byte.valueOf(valueString);
            } catch (Exception nfe) {
                if (!isPrimitive) return null;
                return (T) Byte.valueOf((byte)0);
            }
        }
        if (valueType.isAssignableFrom(Float.class)) {
            try {
                return (T) Float.valueOf(valueString);
            } catch (Exception e) {
                if (!isPrimitive) return null;
                return (T) Float.valueOf(0.0f);
            }
        }
        if (valueType.isAssignableFrom(Double.class)) {
            try {
                return (T) Double.valueOf(valueString);
            } catch (Exception e) {
                if (!isPrimitive) return null;
                return (T) Double.valueOf(0.0);
            }
        }
        if (valueType.isAssignableFrom(String.class)) {
            return (T) valueString;
        }
        if (valueType.isAssignableFrom(Boolean.class)) {
            try {
                return (T) Boolean.valueOf(valueString);
            } catch (Exception e) {
                if (!isPrimitive) return null;
                return (T) Boolean.valueOf(false);
            }
        }

        throw new RuntimeException("Config method type not supported");
    }

    Set<String> getFlags() {
        return new HashSet<>(flags);
    }

    void applyConfigValue(String valueString) {
        this.valueString = valueString;
    }

    private static boolean isPrimitive(Class<?> type) {
        if (boolean.class == type) return true;
        if (byte.class == type) return true;
        if (char.class == type) return true;
        if (double.class == type) return true;
        if (float.class == type) return true;
        if (int.class == type) return true;
        if (long.class == type) return true;
        if (short.class == type) return true;
        return false;
    }

    private static Class<?> boxIfPrimitive(Class<?> type) {
        if (boolean.class == type) return Boolean.class;
        if (byte.class == type) return Byte.class;
        if (char.class == type) return Character.class;
        if (double.class == type) return Double.class;
        if (float.class == type) return Float.class;
        if (int.class == type) return Integer.class;
        if (long.class == type) return Long.class;
        if (short.class == type) return Short.class;
        return type;
    }


}
