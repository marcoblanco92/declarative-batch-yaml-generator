package com.marbl.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StepComponent extends DrawioComponent implements ConditionalFlowHolder{
    private Integer chunkSize;
    private String nextStepId;
    private List<DrawioComponent> nextComponents = new ArrayList<>();
    private Map<String, DrawioComponent> conditionalTargets = new HashMap<>();

    @Override
    public Map<String, DrawioComponent> getConditionalTargets() {
        return conditionalTargets;
    }
}