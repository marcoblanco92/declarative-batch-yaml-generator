package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.mapper.Step;
import com.marbl.generator.model.yml.TransitionYml;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Maps conditionalNext of steps to TransitionYml.
 */
public class TransitionMapper {

    public static List<TransitionYml> mapTransitions(Step dto) {
        Map<String, String> conditionalNext = dto.getConditionalNext();
        if (conditionalNext == null || conditionalNext.isEmpty()) return null;

        return conditionalNext.entrySet().stream()
                .map(entry -> TransitionYml.builder()
                        .from(entry.getValue())
                        .onCondition(entry.getKey())
                        .toStep(dto.getName().concat("Step"))
                        .build())
                .toList();
    }
}
