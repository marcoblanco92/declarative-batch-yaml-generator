package com.marbl.generator.mapper.dto;

import com.marbl.generator.mapper.dto.component.JobYmlMapper;
import com.marbl.generator.mapper.dto.component.LoggingYmlMapper;
import com.marbl.generator.model.mapper.BulkDto;
import com.marbl.generator.model.yml.RootYml;
import lombok.experimental.UtilityClass;

/**
 * Entry point for mapping BulkDto objects into YAML model (RootYml).
 *
 * <p>This class orchestrates the mapping process:
 * <ul>
 *     <li>StepYmlMapper: maps StepDto and TaskletDto to StepYmlBase</li>
 *     <li>ChildYmlMapper: maps Reader, Processor, Writer, and Listeners</li>
 *     <li>TransitionMapper: maps conditionalNext to TransitionYml</li>
 *     <li>JobYmlMapper: maps JobDto to JobYml</li>
 *     <li>LoggingYmlMapper: creates LoggingYml</li>
 * </ul>
 */
@UtilityClass
public class DtoToYmlMapper {

    /**
     * Maps a BulkDto to RootYml with batch, job, datasources, and logging.
     *
     * @param bulkDto            the BulkDto
     * @param loggingBasePackage
     * @return RootYml ready for YAML serialization
     */
    public RootYml mapToRootYaml(BulkDto bulkDto, String loggingBasePackage) {
        return RootYml.builder()
                .bulk(JobYmlMapper.mapBulk(bulkDto))
                .logging(LoggingYmlMapper.mapLogging(loggingBasePackage))
                .build();
    }
}
