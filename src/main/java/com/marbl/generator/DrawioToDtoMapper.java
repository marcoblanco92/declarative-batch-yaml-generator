package com.marbl.generator;

import com.marbl.generator.model.drawio.*;
import com.marbl.generator.model.mapper.*;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class DrawioToDtoMapper {

    public BulkDto mapToBulkDto(DrawioParsed drawioParsed) {

        List<DrawioComponent> components = drawioParsed.getComponents();
        List<DrawioEdge> edges = drawioParsed.getEdges();

        Map<String, DrawioComponent> componentById = components.stream()
                .collect(Collectors.toMap(DrawioComponent::getId, c -> c));

        Map<String, Step> stepsById = new HashMap<>();

        // --- Crea StepYml e TaskletYml ---
        for (DrawioComponent comp : components) {
            if (comp instanceof DrawioStep step) {
                StepDto stepDto = StepDto.builder()
                        .name(step.getName())
                        .chunkSize(step.getChunkSize())
                        .listeners(new ArrayList<>())
                        .conditionalNext(new HashMap<>())
                        .order(step.getOrder())
                        .build();
                stepsById.put(step.getId(), stepDto);
            } else if (comp instanceof DrawioTasklet tasklet) {
                TaskletDto taskletDto = TaskletDto.builder()
                        .name(tasklet.getName())
                        .conditionalNext(new HashMap<>())
                        .order(tasklet.getOrder())
                        .build();
                stepsById.put(tasklet.getId(), taskletDto); // TaskletYml implementa Step
            }
        }

        // --- Associa Reader/Processor/Writer/Listener ai Step ---
        for (DrawioComponent comp : components) {
            Step step = null;
            if (comp instanceof DrawioStep stepComp) {
                step = stepsById.get(stepComp.getId());
            } else if (comp instanceof DrawioTasklet taskletComp) {
                step = stepsById.get(taskletComp.getId());
            }
            if (step == null) continue;

            for (DrawioComponent child : components) {
                if (child.getParentId() != null && child.getParentId().equals(comp.getId()) && step instanceof StepDto stepDto) {

                    if (child instanceof DrawioReader reader) {
                        stepDto.setReader(ReaderDto.builder()
                                .name(reader.getName())
                                .type(reader.getReaderType())
                                .build());
                    } else if (child instanceof DrawioProcessor processor) {
                        stepDto.setProcessor(ProcessorDto.builder()
                                .name(processor.getName())
                                .type(processor.getProcessorType())
                                .build());
                    } else if (child instanceof DrawioWriter writer) {
                        stepDto.setWriter(WriterDto.builder()
                                .name(writer.getName())
                                .type(writer.getWriterType())
                                .build());
                    } else if (child instanceof DrawioListeners listeners) {
                        String listenersContainerId = listeners.getId();

                        components.stream()
                                .filter(component -> listenersContainerId.equals(component.getParentId()))
                                .forEach(listener -> {
                                    ListenerDto dto = ListenerDto.builder()
                                            .name(listener.getName())
                                            .type(listener.getType())
                                            .build();
                                    stepDto.getListeners().add(dto);
                                });
                    }
                }
            }
        }

        // --- Gestione flussi NEXT / SIMPLE_FLOW / ON_CONDITION ---
        for (DrawioEdge edge : edges) {
            DrawioComponent sourceComp = componentById.get(edge.getSourceId());
            DrawioComponent targetComp = componentById.get(edge.getTargetId());

            if (sourceComp == null || targetComp == null) continue;

            // Risaliamo allo Step o Tasklet "padre" del source
            Step sourceStep = (sourceComp instanceof DrawioStep || sourceComp instanceof DrawioTasklet)
                    ? stepsById.get(sourceComp.getId())
                    : (sourceComp.getParentId() != null ? stepsById.get(sourceComp.getParentId()) : null);

            // Risaliamo allo Step o Tasklet "padre" del target
            Step targetStep = (targetComp instanceof DrawioStep || targetComp instanceof DrawioTasklet)
                    ? stepsById.get(targetComp.getId())
                    : (targetComp.getParentId() != null ? stepsById.get(targetComp.getParentId()) : null);

            if (sourceStep == null || targetStep == null) continue;

            System.out.println(sourceStep.getName() + " -> " + targetStep.getName() + " -> " + edge.getType());
            switch (edge.getType()) {
                case NEXT -> {
                    // NEXT o SIMPLE_FLOW: source punta al target
                    sourceStep.setNextStep(targetStep.getName());
                }
                case ON_CONDITION -> {
                    // ON_CONDITION: target registra la condizione riferita dal source
                    String conditionKey = edge.getCondition() != null ? edge.getCondition() : "DEFAULT";
                    targetStep.getConditionalNext().put(conditionKey, sourceStep.getName());
                }
                default -> {}
            }
        }

        // --- Crea JobYml ---
        DrawioJob jobComp = components.stream()
                .filter(c -> c instanceof DrawioJob)
                .map(c -> (DrawioJob) c)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nessun Job trovato"));

        JobDto job = JobDto.builder()
                .name(jobComp.getName())
                .steps(new ArrayList<>(stepsById.values()))
                .build();

        // --- Crea DataSourceYml ---
        List<DataSourceDto> dataSources = components.stream()
                .filter(c -> c instanceof DrawioDataSource)
                .map(c -> {
                    DrawioDataSource ds = (DrawioDataSource) c;
                    return DataSourceDto.builder()
                            .name(ds.getName())
                            .main(ds.isMain())
                            .properties(new HashMap<>())
                            .build();
                })
                .toList();

        return BulkDto.builder()
                .job(job)
                .dataSources(dataSources)
                .build();
    }

}
