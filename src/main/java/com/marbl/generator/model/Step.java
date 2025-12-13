package com.marbl.generator.model;

import java.util.Map;

public interface Step {

    String getName();
    void setNextStep(String nextStep);
    Map<String, String> getConditionalNext();
}
