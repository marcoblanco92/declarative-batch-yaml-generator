package com.marbl.generator;

import com.marbl.generator.model.mapper.*;
import com.marbl.generator.model.yml.*;
import com.marbl.generator.model.yml.reader.PreConfiguredReader;
import com.marbl.generator.model.yml.writer.PreConfiguredWriter;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DtoToYmlMapper {

    public RootYml mapToRootYaml(BulkDto bulkDto) {
        return RootYml.builder()
                .bulk(mapBulk(bulkDto))
                .logging(mapLogging(bulkDto))
                .build();
    }

    private BulkYml mapBulk(BulkDto dto) {
        return BulkYml.builder()
                .batchProperties(mapBatchProperties(dto))
                .datasources(mapDatasources(dto))
                .batchJob(mapJob(dto.getJob()))
                .build();
    }

    private BatchPropertiesYml mapBatchProperties(BulkDto dto) {
        return BatchPropertiesYml.builder()
                .jdbc(JdbcPropertiesYml.builder()
                        .platform("")
                        .initializeSchema("never")
                        .tablePrefix("BATCH_")
                        .build())
                .build();
    }

    private Map<String, DataSourceYml> mapDatasources(BulkDto dto) {
        return dto.getDataSources().stream()
                .collect(Collectors.toMap(
                        DataSourceDto::getName,
                        this::mapDatasource
                ));
    }

    private DataSourceYml mapDatasource(DataSourceDto dto) {
        return DataSourceYml.builder()
                .name(dto.getName())
                .main(dto.isMain())
                .url("")
                .username("")
                .password("")
                .type(dto.getType())
                .driverClassName("")
                .build();
    }

    private JobYml mapJob(JobDto dto) {
        return JobYml.builder()
                .name(dto.getName())
                .steps(mapSteps(dto.getSteps()))
                .listener(mapJobListener(dto))
                .build();
    }

    private List<StepYmlBase> mapSteps(List<Step> steps) {
        return steps.stream()
                .sorted(Comparator.comparingInt(Step::getOrder))
                .map(this::mapStep)
                .toList();
    }

    private StepYmlBase mapStep(Step dto) {

        if (dto instanceof TaskletDto) {
            return mapTasklet((TaskletDto) dto);
        }

        return mapChunkStep((StepDto) dto);
    }

    private StepYml mapChunkStep(StepDto dto) {

        return StepYml.builder()
                .name(dto.getName())
                .chunk(dto.getChunkSize())
                .reader(mapReader(dto.getReader()))
                .processor(mapProcessor(dto.getProcessor()))
                .writer(mapWriter(dto.getWriter()))
                .listeners(mapStepListeners(dto))
                .next(dto.getNextStep())
                .transitions(mapTransitions(dto))
                .build();
    }

    private TaskletYml mapTasklet(TaskletDto dto) {

        return TaskletYml.builder()
                .name(dto.getName().concat("Step"))
                .type("TASKLET")
                .tasklet(dto.getName())
                .next(dto.getNextStep())
                .transitions(mapTransitions(dto))
                .build();
    }

    private List<TransitionYml> mapTransitions(Step dto) {
        if (dto.getConditionalNext() == null || dto.getConditionalNext().isEmpty()) {
            return null;
        }

        return dto.getConditionalNext().entrySet()
                .stream()
                .map(entry -> TransitionYml.builder()
                        .from(entry.getValue())
                        .onCondition(entry.getKey())
                        .toStep(dto.getName().concat("Step"))
                        .build()
                )
                .toList();
    }


    private ReaderYml mapReader(ReaderDto dto) {
        if (dto == null) return null;

        return ReaderYml.builder()
                .name(dto.getName())
                .type(dto.getType())
                .config(PreConfiguredReader.getConfigByType(dto.getType()))
                .build();
    }

    private ProcessorYml mapProcessor(ProcessorDto dto) {
        if (dto == null) return null;

        return ProcessorYml.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }

    private WriterYml mapWriter(WriterDto dto) {
        if (dto == null) return null;

        return WriterYml.builder()
                .name(dto.getName())
                .type(dto.getType())
                .config(PreConfiguredWriter.getConfigByType(dto.getType()))
                .build();
    }

    private ListenerYml mapJobListener(JobDto dto) {
        if (dto.getListener() == null) return null;

        return ListenerYml.builder()
                .name(dto.getListener().getName())
                .type(dto.getListener().getType())
                .build();
    }


    private List<ListenerYml> mapStepListeners(StepDto dto) {
        if (dto.getListeners() == null) return null;

        return dto.getListeners().stream()
                .map(l -> ListenerYml.builder()
                        .name(l.getName())
                        .type(l.getType())
                        .build())
                .toList();
    }

    private LoggingYml mapLogging(BulkDto dto) {
        return LoggingYml.builder()
                .level(Map.of(
                        "root", "INFO",
                        "com.marbl.xxxxx", "DEBUG"
                ))
                .pattern(LoggingPatternYml.builder()
                        .console("[%d{dd-MM-yyyy HH:mm:ss.SSSS}] ...")
                        .build())
                .build();
    }


}
