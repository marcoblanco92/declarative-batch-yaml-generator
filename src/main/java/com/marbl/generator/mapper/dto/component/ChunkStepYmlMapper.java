package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.mapper.StepDto;
import com.marbl.generator.model.yml.StepYml;

/**
 * Maps StepDto (chunk-based step) to StepYml.
 */
public class ChunkStepYmlMapper {

    public static StepYml mapChunkStep(StepDto dto) {
        return StepYml.builder()
                .name(dto.getName())
                .chunk(dto.getChunkSize())
                .reader(ChildYmlMapper.mapReader(dto.getReader()))
                .processor(ChildYmlMapper.mapProcessor(dto.getProcessor()))
                .writer(ChildYmlMapper.mapWriter(dto.getWriter()))
                .listeners(ChildYmlMapper.mapStepListeners(dto))
                .next(dto.getNextStep())
                .transitions(TransitionMapper.mapTransitions(dto))
                .build();
    }
}
