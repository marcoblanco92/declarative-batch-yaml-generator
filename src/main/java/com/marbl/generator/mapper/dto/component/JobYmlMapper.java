package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.mapper.*;
import com.marbl.generator.model.yml.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Maps JobDto and DataSourceDto to JobYml and BulkYml.
 */
public class JobYmlMapper {

    public static BulkYml mapBulk(BulkDto dto) {
        return BulkYml.builder()
                .batchProperties(mapBatchProperties(dto))
                .datasources(mapDatasources(dto))
                .batchJob(mapJob(dto.getJob()))
                .build();
    }

    private static BatchPropertiesYml mapBatchProperties(BulkDto dto) {
        return BatchPropertiesYml.builder()
                .jdbc(JdbcPropertiesYml.builder()
                        .platform("")
                        .initializeSchema("never")
                        .tablePrefix("BATCH_")
                        .build())
                .build();
    }

    private static Map<String, DataSourceYml> mapDatasources(BulkDto dto) {
        return dto.getDataSources().stream()
                .collect(Collectors.toMap(
                        DataSourceDto::getName,
                        ChildYmlMapper::mapDataSource
                ));
    }

    private static JobYml mapJob(JobDto dto) {
        return JobYml.builder()
                .name(dto.getName())
                .steps(StepYmlMapper.mapSteps(dto.getSteps()))
                .listener(ChildYmlMapper.mapJobListener(dto))
                .build();
    }
}
