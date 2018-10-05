package com.spoohapps.farcommon;

import com.spoohapps.farcommon.config.ConfigBuilder;

public class Config {

    public static <T> ConfigBuilder<T> from(Class<T> clazz) {
        return new ConfigBuilder<>(clazz);
    }

}
