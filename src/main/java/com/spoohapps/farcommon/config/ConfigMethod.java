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
        Type returnType = method.getGenericReturnType();
        if (returnType == void.class) {
            throw new IllegalArgumentException("Config methods must not return void.");
        }
        if (method.getParameterCount() != 0) {
            throw new IllegalArgumentException("Config methods must not define formal parameters");
        }
        //TODO: further validation for return types?
        return new ConfigMethod<>(method);
    }

    @SuppressWarnings("unchecked")
    private ConfigMethod(Method method) {
        this.method = method;
        Arrays.stream(method.getAnnotations())
                .forEach(this::parseMethodAnnotation);
    }

    private void parseMethodAnnotation(Annotation annotation) {
        if (annotation instanceof ConfigFlags) {
            //TODO validation for flags
            flags.addAll(Arrays.asList(((ConfigFlags) annotation).value()));
        }
    }

    @SuppressWarnings("unchecked")
    T invoke(Object[] args) {
        Class<?> valueType = boxIfPrimitive(method.getReturnType());

        if (valueType.isAssignableFrom(Integer.class)) {
            return (T) Integer.valueOf(valueString);
        }
        if (valueType.isAssignableFrom(Long.class)) {
            return (T) Long.valueOf(valueString);
        }
        if (valueType.isAssignableFrom(Short.class)) {
            return (T) Short.valueOf(valueString);
        }
        if (valueType.isAssignableFrom(Byte.class)) {
            return (T) Byte.valueOf(valueString);
        }
        if (valueType.isAssignableFrom(Float.class)) {
            return (T) Float.valueOf(valueString);
        }
        if (valueType.isAssignableFrom(Double.class)) {
            return (T) Double.valueOf(valueString);
        }
        if (valueType.isAssignableFrom(String.class)) {
            return (T) valueString;
        }
        if (valueType.isAssignableFrom(Boolean.class)) {
            return (T) Boolean.valueOf(valueString);
        }

        throw new RuntimeException("Config method type not supported");
    }

    Set<String> getFlags() {
        return new HashSet<>(flags);
    }

    void applyConfigValue(String valueString) {
        this.valueString = valueString;
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
