package com.marbl.generator.model.mapper;

import java.util.Map;

public interface Step {

    String getName();
    void setNextStep(String nextStep);
    Map<String, String> getConditionalNext();
}
