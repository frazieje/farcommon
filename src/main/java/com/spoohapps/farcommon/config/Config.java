package com.spoohapps.farcommon.config;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;

public final class Config {

    @SuppressWarnings("unchecked")
    public static <T> Config.Builder from(Class<T> clazz) {
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("Config contracts must be interfaces.");
        }
        return new Builder(clazz);
    }

    public static class Builder<T> {

        Class<T> typeRef;

        Builder(Class<T> clazz) {
            typeRef = clazz;
        }

        @SuppressWarnings("unchecked")
        public T build() {

            return (T) Proxy.newProxyInstance(typeRef.getClassLoader(), new Class<?>[]{ typeRef },
                    (proxy, method, args) -> {
                        if (method.getDeclaringClass() == Object.class) {
                            return method.invoke(Builder.this, args);
                        }
                        if (method.isDefault()) {
                            Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                            constructor.setAccessible(true);
                            return constructor.newInstance(typeRef, -1)
                                    .unreflectSpecial(method, typeRef)
                                    .bindTo(proxy)
                                    .invokeWithArguments(args);
                        }
                        return ConfigMethod.parseAnnotations(method).invoke(args != null ? args : new Object[0]);
                    });

        }

    }

}
