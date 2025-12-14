package com.marbl.generator.model.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StepDto implements Step {
    private String name;
    private Integer chunkSize;
    private ReaderDto reader;
    private ProcessorDto processor;
    private WriterDto writer;
    @Builder.Default
    private List<ListenerDto> listeners = new ArrayList<>();
    @Builder.Default
    private Map<String, String> conditionalNext = new HashMap<>();
    private String nextStep;
    private Integer order;
}
