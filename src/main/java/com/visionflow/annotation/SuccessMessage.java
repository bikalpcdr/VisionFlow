package com.visionflow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author bikalpa.chaudharii
 * @project visionflow
 * @created 26/7/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SuccessMessage {
    String entity();

    String action() default "created";
}
