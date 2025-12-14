package com.marbl.generator.model.mapper;

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
public class TaskletDto implements Step {
    private String name;
    private Map<String, String> conditionalNext = new HashMap<>(); // flussi condizionali: condizione -> stepName
    private String nextStep;
    private Integer order;
}