package com.marbl.generator.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskletYml implements Step {
    private String name;
    private Map<String, String> conditionalNext = new HashMap<>(); // flussi condizionali: condizione -> stepName
    private String nextStep;
}