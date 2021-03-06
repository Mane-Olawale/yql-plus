/*
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.yqlplus.api.annotations;

import java.lang.annotation.*;

/**
 * Indicate a given argument should be injected.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface Trace {
    /**
     * *Name* of tracer to inject.
     */
    String value();

    /**
     * Group of tracer to inject (will default to class::method).
     */
    String group() default "";
}
