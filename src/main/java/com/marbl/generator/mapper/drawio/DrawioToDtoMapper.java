package com.marbl.generator.mapper.drawio;

import com.marbl.generator.mapper.drawio.component.ChildMapper;
import com.marbl.generator.mapper.drawio.component.EdgeMapper;
import com.marbl.generator.mapper.drawio.component.JobMapper;
import com.marbl.generator.mapper.drawio.component.StepMapper;
import com.marbl.generator.model.drawio.DrawioParsed;
import com.marbl.generator.model.mapper.BulkDto;
import lombok.experimental.UtilityClass;

/**
 * Entry point for mapping Draw.io parsed objects into DTOs.
 *
 * <p>This class orchestrates the mapping process:
 * <ul>
 *     <li>StepMapper: maps DrawioStep / DrawioTasklet to StepDto / TaskletDto</li>
 *     <li>ChildMapper: maps Readers, Processors, Writers, and Listeners to steps</li>
 *     <li>EdgeMapper: maps DrawioEdge objects to step transitions (nextStep / conditionalNext)</li>
 *     <li>JobMapper: maps DrawioJob to JobDto</li>
 * </ul>
 */
@UtilityClass
public class DrawioToDtoMapper {

    /**
     * Maps a DrawioParsed object to a BulkDto suitable for YAML generation.
     *
     * @param drawioParsed parsed Draw.io model containing components and edges
     * @return BulkDto with job, steps, data sources, and flow information
     */
    public BulkDto mapToBulkDto(DrawioParsed drawioParsed) {
        // 1️⃣ Map steps and tasklets
        var stepsById = StepMapper.mapSteps(drawioParsed.getComponents());

        // 2️⃣ Map child components (Reader, Processor, Writer, Listeners)
        ChildMapper.mapChildren(drawioParsed.getComponents(), stepsById);

        // 3️⃣ Map edges to nextStep and conditionalNext
        EdgeMapper.mapEdges(drawioParsed.getEdges(), stepsById, drawioParsed.getComponents());

        // 4️⃣ Map job
        var jobDto = JobMapper.mapJob(drawioParsed.getComponents(), stepsById);

        // 5️⃣ Map datasources
        var dataSources = drawioParsed.getComponents().stream()
                .filter(c -> c instanceof com.marbl.generator.model.drawio.DrawioDataSource)
                .map(JobMapper::mapDataSource)
                .toList();

        return BulkDto.builder()
                .job(jobDto)
                .dataSources(dataSources)
                .build();
    }
}
