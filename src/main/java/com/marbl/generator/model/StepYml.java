package com.marbl.generator.model;

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
public class StepYml implements Step {
    private String name;
    private Integer chunkSize;          // se chunk-oriented
    private ReaderYml reader;
    private ProcessorYml processor;
    private WriterYml writer;
    private List<ListenerYml> listeners = new ArrayList<>();
    private Map<String, String> conditionalNext = new HashMap<>(); // flussi condizionali: condizione -> stepName
    private String nextStep;            // step successivo (Next/SimpleFlow)
}
