package com.marbl.generator;

import com.marbl.generator.model.*;
import com.marbl.generator.dto.*;

import java.util.*;
import java.util.stream.Collectors;

public class DrawioToYamlMapper {

    public BulkYml mapToBulkYml(ParsedDrawio parsedDrawio) {

        List<DrawioComponent> components = parsedDrawio.getComponents();
        List<DrawioEdge> edges = parsedDrawio.getEdges();

        Map<String, DrawioComponent> componentById = components.stream()
                .collect(Collectors.toMap(DrawioComponent::getId, c -> c));

        Map<String, Step> stepsById = new HashMap<>();

        // --- Crea StepYml e TaskletYml ---
        for (DrawioComponent comp : components) {
            if (comp instanceof StepComponent step) {
                StepYml stepYml = StepYml.builder()
                        .name(step.getName())
                        .chunkSize(step.getChunkSize())
                        .listeners(new ArrayList<>())
                        .conditionalNext(new HashMap<>())
                        .build();
                stepsById.put(step.getId(), stepYml);
            } else if (comp instanceof TaskletComponent tasklet) {
                TaskletYml taskletYml = TaskletYml.builder()
                        .name(tasklet.getName())
                        .conditionalNext(new HashMap<>())
                        .build();
                stepsById.put(tasklet.getId(), taskletYml); // TaskletYml implementa Step
            }
        }

        // --- Associa Reader/Processor/Writer/Listener ai Step ---
        for (DrawioComponent comp : components) {
            Step step = null;
            if (comp instanceof StepComponent stepComp) {
                step = stepsById.get(stepComp.getId());
            } else if (comp instanceof TaskletComponent taskletComp) {
                step = stepsById.get(taskletComp.getId());
            }
            if (step == null) continue;

            for (DrawioComponent child : components) {
                if (child.getParentId() != null && child.getParentId().equals(comp.getId()) && step instanceof StepYml stepYml) {

                    if (child instanceof ReaderComponent reader) {
                        stepYml.setReader(ReaderYml.builder()
                                .name(reader.getName())
                                .type(reader.getReaderType())
                                .properties(new HashMap<>())
                                .build());
                    } else if (child instanceof ProcessorComponent processor) {
                        stepYml.setProcessor(ProcessorYml.builder()
                                .name(processor.getName())
                                .type(processor.getProcessorType())
                                .build());
                    } else if (child instanceof WriterComponent writer) {
                        stepYml.setWriter(WriterYml.builder()
                                .name(writer.getName())
                                .type(writer.getWriterType())
                                .properties(new HashMap<>())
                                .build());
                    } else if (child instanceof ListenerComponent listener) {
                        stepYml.getListeners().add(ListenerYml.builder()
                                .name(listener.getName())
                                .type(listener.getListenerType())
                                .build());
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
            Step sourceStep = (sourceComp instanceof StepComponent || sourceComp instanceof TaskletComponent)
                    ? stepsById.get(sourceComp.getId())
                    : (sourceComp.getParentId() != null ? stepsById.get(sourceComp.getParentId()) : null);

            // Risaliamo allo Step o Tasklet "padre" del target
            Step targetStep = (targetComp instanceof StepComponent || targetComp instanceof TaskletComponent)
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
        JobComponent jobComp = components.stream()
                .filter(c -> c instanceof JobComponent)
                .map(c -> (JobComponent) c)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Nessun Job trovato"));

        JobYml job = JobYml.builder()
                .name(jobComp.getName())
                .steps(new ArrayList<>(stepsById.values()))
                .build();

        // --- Crea DataSourceYml ---
        List<DataSourceYml> dataSources = components.stream()
                .filter(c -> c instanceof DataSourceComponent)
                .map(c -> {
                    DataSourceComponent ds = (DataSourceComponent) c;
                    return DataSourceYml.builder()
                            .name(ds.getName())
                            .main(ds.isMain())
                            .properties(new HashMap<>())
                            .build();
                })
                .toList();

        return BulkYml.builder()
                .job(job)
                .dataSources(dataSources)
                .build();
    }

}
