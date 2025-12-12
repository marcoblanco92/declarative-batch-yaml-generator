package com.marbl.generator.model;

import java.util.HashMap;
import java.util.Map;

public interface ConditionalFlowHolder {

    Map<String, DrawioComponent> getConditionalTargets();

    default void addConditionalTarget(String condition, DrawioComponent target) {
        getConditionalTargets().put(condition, target);
    }
}
