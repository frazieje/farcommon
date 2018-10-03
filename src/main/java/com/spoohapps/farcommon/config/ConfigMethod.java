package com.spoohapps.farcommon.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ConfigMethod<T> {

    private final Set<String> flags = ConcurrentHashMap.newKeySet();

    private final T value;

    static <T> ConfigMethod<T> parseAnnotations(Method method) {
        Type returnType = method.getGenericReturnType();
        if (returnType == void.class) {
            throw new IllegalArgumentException("Config methods cannot return void.");
        }
        //TODO: further validation for return types?
        return new ConfigMethod<>(method);
    }

    @SuppressWarnings("unchecked")
    private ConfigMethod(Method method) {
        try {
            value = (T) method.getGenericReturnType().getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
        Arrays.stream(method.getAnnotations())
                .forEach(this::parseMethodAnnotation);
    }

    private void parseMethodAnnotation(Annotation annotation) {
        if (annotation instanceof ConfigFlags) {
            //TODO validation for flags
            flags.addAll(Arrays.asList(((ConfigFlags) annotation).value()));
        }
    }

    T invoke(Object[] args) {
        return value;
    }

    boolean hasFlag(String flag) {
        return flags.contains(flag);
    }


}
