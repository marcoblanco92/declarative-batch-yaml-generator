package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.mapper.*;
import com.marbl.generator.model.yml.StepYmlBase;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps StepDto and TaskletDto to StepYmlBase.
 */
public class StepYmlMapper {

    public static List<StepYmlBase> mapSteps(List<Step> steps) {
        return steps.stream()
                .sorted(Comparator.comparingInt(Step::getOrder))
                .map(StepYmlMapper::mapStep)
                .toList();
    }

    private static StepYmlBase mapStep(Step dto) {
        if (dto instanceof TaskletDto tasklet) {
            return TaskletYmlMapper.mapTasklet(tasklet);
        }
        return ChunkStepYmlMapper.mapChunkStep((StepDto) dto);
    }
}
