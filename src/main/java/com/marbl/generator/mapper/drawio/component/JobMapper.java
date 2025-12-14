package com.marbl.generator.mapper.drawio.component;

import com.marbl.generator.model.drawio.*;
import com.marbl.generator.model.mapper.*;
import java.util.*;

/**
 * Maps DrawioJob and DrawioDataSource objects to JobDto and DataSourceDto.
 */
public class JobMapper {

    public static JobDto mapJob(List<DrawioComponent> components, Map<String, Step> stepsById) {
        DrawioJob jobComp = components.stream()
                .filter(c -> c instanceof DrawioJob)
                .map(c -> (DrawioJob) c)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No Job found"));

        return JobDto.builder()
                .name(jobComp.getName())
                .steps(new ArrayList<>(stepsById.values()))
                .build();
    }

    public static DataSourceDto mapDataSource(DrawioComponent component) {
        DrawioDataSource ds = (DrawioDataSource) component;
        return DataSourceDto.builder()
                .name(ds.getName())
                .main(ds.isMain())
                .properties(new HashMap<>())
                .build();
    }
}
