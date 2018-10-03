package com.spoohapps.farcommon.config;

import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;

@Target(METHOD)
public @interface ConfigFlags {

    String[] value() default "";

}
