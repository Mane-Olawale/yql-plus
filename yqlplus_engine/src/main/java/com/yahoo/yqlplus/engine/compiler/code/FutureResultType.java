/*
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.yqlplus.engine.compiler.code;

import com.yahoo.yqlplus.api.types.YQLCoreType;
import org.objectweb.asm.Type;

import java.util.concurrent.Future;

public class FutureResultType extends BaseTypeWidget {
    private final TypeWidget valueType;

    public FutureResultType(TypeWidget valueType) {
        super(Type.getType(Future.class));
        this.valueType = valueType;
    }

    @Override
    public YQLCoreType getValueCoreType() {
        return YQLCoreType.PROMISE;
    }


    @Override
    public boolean isPromise() {
        return true;
    }

    @Override
    public PromiseAdapter getPromiseAdapter() {
        return new FuturePromiseAdapter(valueType);
    }

    @Override
    public boolean hasUnificationAdapter() {
        return true;
    }

    @Override
    public UnificationAdapter getUnificationAdapter(final EngineValueTypeAdapter typeAdapter) {
        return new UnificationAdapter() {
            @Override
            public TypeWidget unify(TypeWidget other) {
                PromiseAdapter promiseAdapter = other.getPromiseAdapter();
                return new FutureResultType(typeAdapter.unifyTypes(valueType, promiseAdapter.getResultType()));
            }
        };
    }

}