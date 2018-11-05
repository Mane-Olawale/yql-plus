/*
 * Copyright (c) 2016 Yahoo Inc.
 * Licensed under the terms of the Apache version 2.0 license.
 * See LICENSE file for terms.
 */

package com.yahoo.yqlplus.operator;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.yahoo.yqlplus.api.types.Annotations;
import com.yahoo.yqlplus.engine.compiler.code.EngineValueTypeAdapter;
import com.yahoo.yqlplus.language.operator.OperatorNode;
import com.yahoo.yqlplus.language.operator.OperatorTreeVisitor;
import com.yahoo.yqlplus.language.parser.Location;

import java.util.Set;

public final class OperatorStep implements Step {
    public static OperatorValue create(EngineValueTypeAdapter typeAdapter, Location loc, PhysicalOperator operator, Object... arguments) {
        OperatorNode<PhysicalOperator> compute = OperatorNode.create(loc, operator, arguments);
        OperatorValue value = new OperatorValue(operator.hasResult(typeAdapter, compute), Annotations.EMPTY);
        // sets step on value
        new OperatorStep(compute, value);
        return value;
    }

    public static OperatorValue create(EngineValueTypeAdapter typeAdapter, PhysicalOperator operator, Object... arguments) {
        OperatorNode<PhysicalOperator> compute = OperatorNode.create(operator, arguments);
        OperatorValue value = new OperatorValue(operator.hasResult(typeAdapter, compute), Annotations.EMPTY);
        // sets step on value
        new OperatorStep(compute, value);
        return value;
    }


    private final OperatorNode<PhysicalOperator> compute;
    private final OperatorValue value;
    private Set<Value> before = ImmutableSet.of();

    private OperatorStep(OperatorNode<PhysicalOperator> compute, OperatorValue value) {
        this.compute = compute;
        this.value = value;
        value.setStep(this);
    }

    public OperatorNode<PhysicalOperator> getCompute() {
        return compute;
    }

    @Override
    public Set<? extends Value> getInputs() {
        if (compute != null) {
            final Set<Value> inputs = Sets.newIdentityHashSet();
            compute.visitNode(new OperatorTreeVisitor() {
                @Override
                public void visit(Object arg) {
                    if (arg instanceof Value) {
                        inputs.add((Value) arg);
                    }
                }
            });
            inputs.addAll(before);
            return inputs;
        } else {
            return before;
        }
    }

    @Override
    public OperatorValue getOutput() {
        return value;
    }
}