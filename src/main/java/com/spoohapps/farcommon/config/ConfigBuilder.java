package com.spoohapps.farcommon.config;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigBuilder<T> {

    private final ConcurrentHashMap<String, ConfigMethod<T>> methodCache = new ConcurrentHashMap<>();

    private Class<T> typeRef;

    public ConfigBuilder(Class<T> clazz) {
        typeRef = clazz;
        Arrays.stream(clazz.getDeclaredMethods())
                .forEach(this::loadConfigMethod);
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
                    return cfm.invoke(args != null ? args : new Object[0]);
                });

    }

    private ConfigMethod<T> loadConfigMethod(Method method) {
        ConfigMethod<T> result = methodCache.get(method.getName());
        if (result != null) return result;

        synchronized (methodCache) {
            result = methodCache.get(method.getName());
            if (result == null) {
                result = ConfigMethod.parseAnnotations(method);
                methodCache.put(method.getName(), result);
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

            methodCache.values()
                    .forEach(m -> prop.stringPropertyNames()
                            .forEach(p -> {
                                if (m.getFlags().contains(p)) {
                                    m.applyConfigValue(prop.getProperty(p));
                                }
                            }));

            return this;

        } catch (IOException ex) {
            ex.printStackTrace();
            return this;
        }
    }

    public ConfigBuilder<T> apply(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("-") && i != (args.length - 1)) {
                String arg = args[i].substring(1);
                String val = args[i + 1];
                methodCache.values()
                        .forEach(m -> {
                            if (m.getFlags().contains(arg)) {
                                m.applyConfigValue(val);
                            }
                        });
            }
        }
        return this;
    }

    public ConfigBuilder<T> apply(T other) {
        Arrays.stream(other.getClass().getDeclaredMethods())
                .filter(method ->
                        method.getDeclaringClass() != Object.class
                        && Modifier.isPublic(method.getModifiers())
                        && method.getParameterCount() == 0
                        && !method.isDefault())
                .forEach(method -> {
                    try {
                        methodCache.get(method.getName()).applyConfigValue(method.invoke(other).toString());
                    } catch (NullPointerException | IllegalAccessException | InvocationTargetException ignored) {}
                });
        return this;
    }

}