package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.mapper.TaskletDto;
import com.marbl.generator.model.yml.TaskletYml;

/**
 * Maps TaskletDto to TaskletYml.
 */
public class TaskletYmlMapper {

    public static TaskletYml mapTasklet(TaskletDto dto) {
        return TaskletYml.builder()
                .name(dto.getName().concat("Step"))
                .type("TASKLET")
                .tasklet(dto.getName())
                .next(dto.getNextStep())
                .transitions(TransitionMapper.mapTransitions(dto))
                .build();
    }
}
