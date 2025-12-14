package com.marbl.generator.mapper.drawio.component;

import com.marbl.generator.model.drawio.*;
import com.marbl.generator.model.mapper.*;
import java.util.*;

/**
 * Maps DrawioStep and DrawioTasklet objects to StepDto and TaskletDto.
 */
public class StepMapper {

    public static Map<String, Step> mapSteps(List<DrawioComponent> components) {
        Map<String, Step> stepsById = new HashMap<>();

        for (DrawioComponent comp : components) {
            if (comp instanceof DrawioStep step) {
                stepsById.put(step.getId(), StepDto.builder()
                        .name(step.getName())
                        .chunkSize(step.getChunkSize())
                        .listeners(new ArrayList<>())
                        .conditionalNext(new HashMap<>())
                        .order(step.getOrder())
                        .build());
            } else if (comp instanceof DrawioTasklet tasklet) {
                stepsById.put(tasklet.getId(), TaskletDto.builder()
                        .name(tasklet.getName())
                        .order(tasklet.getOrder())
                        .conditionalNext(new HashMap<>())
                        .build());
            }
        }
        return stepsById;
    }
}
