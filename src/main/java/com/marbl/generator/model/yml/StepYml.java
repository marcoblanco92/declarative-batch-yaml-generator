package com.marbl.generator.model.yml;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepYml implements StepYmlBase {

    private String name;

    private Integer chunk;

    private ReaderYml reader;

    private ProcessorYml processor;

    private WriterYml writer;

    private List<ListenerYml> listeners;

    private String next;

    private Map<String, String> conditionalNext;

    private List<TransitionYml> transitions;
}
