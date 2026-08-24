package com.next.engine.annotations.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an API as experimental and prone to changes. Please do not use Experimental APIs in production.
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.METHOD })
public @interface Experimental {
    Level level() default Level.WARNING;

    enum Level {
        WARNING, ERROR
    }
}
