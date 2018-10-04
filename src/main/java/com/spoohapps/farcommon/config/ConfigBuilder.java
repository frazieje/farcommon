package com.spoohapps.farcommon.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigBuilder<T> {

    private final ConcurrentHashMap<Method, ConfigMethod<T>> methodCache = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> flagCache = new ConcurrentHashMap<>();

    private Class<T> typeRef;

    public ConfigBuilder(Class<T> clazz) {
        typeRef = clazz;
    }

    @SuppressWarnings("unchecked")
    public T build() {

        return (T) Proxy.newProxyInstance(typeRef.getClassLoader(), new Class<?>[]{ typeRef },
                (proxy, method, args) -> {
                    if (method.getDeclaringClass() == Object.class) {
                        return method.invoke(ConfigBuilder.this, args);
                    }
                    if (method.isDefault()) {
                        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
                        constructor.setAccessible(true);
                        return constructor.newInstance(typeRef, -1)
                                .unreflectSpecial(method, typeRef)
                                .bindTo(proxy)
                                .invokeWithArguments(args);
                    }

                    ConfigMethod<T> cfm = loadConfigMethod(method);
                    cfm.getFlags().forEach(f -> cfm.applyConfigValue(flagCache.get(f)));
                    return cfm.invoke(args != null ? args : new Object[0]);
                });

    }

    private ConfigMethod<T> loadConfigMethod(Method method) {
        ConfigMethod<T> result = methodCache.get(method);
        if (result != null) return result;

        synchronized (methodCache) {
            result = methodCache.get(method);
            if (result == null) {
                result = ConfigMethod.parseAnnotations(method);
                methodCache.put(method, result);
            }
        }
        return result;
    }

    public ConfigBuilder<T> apply(InputStream stream) {
        Properties prop = new Properties();

        try {

            if (stream == null)
                return this;

            prop.load(stream);

            prop.stringPropertyNames().forEach(p -> flagCache.put(p, prop.getProperty(p)));

            return this;

        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ConfigBuilder<T> apply(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-") && i != (args.length - 1)) {
                String arg = args[i].substring(1);
                String val = args[i + 1];
                flagCache.put(arg, val);
            }
        }
        return this;
    }

    public ConfigBuilder<T> apply(T other) {
        Arrays.stream(other.getClass().getDeclaredMethods())
                .filter(method -> Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0)
                .forEach(method -> {
                    try {
                        loadConfigMethod(method).applyConfigValue(method.invoke(other).toString());
                    } catch (IllegalAccessException
                            | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
        return this;
    }

}