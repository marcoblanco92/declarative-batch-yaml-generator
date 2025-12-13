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
    private Integer chunkSize;          // se chunk-oriented
    private ReaderDto reader;
    private ProcessorDto processor;
    private WriterDto writer;
    private List<ListenerDto> listeners = new ArrayList<>();
    private Map<String, String> conditionalNext = new HashMap<>(); // flussi condizionali: condizione -> stepName
    private String nextStep;            // step successivo (Next/SimpleFlow)
}
