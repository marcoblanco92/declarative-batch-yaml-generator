package com.marbl.generator.mapper.drawio.component;

import com.marbl.generator.model.drawio.*;
import com.marbl.generator.model.mapper.*;
import java.util.*;

/**
 * Maps child components (Reader, Processor, Writer, Listeners) to parent steps.
 */
public class ChildMapper {

    public static void mapChildren(List<DrawioComponent> components, Map<String, Step> stepsById) {
        for (DrawioComponent comp : components) {
            Step step = stepsById.get(comp.getId());
            if (step == null) continue;

            if (step instanceof StepDto stepDto) {
                for (DrawioComponent child : components) {
                    if (comp.getId().equals(child.getParentId())) {
                        if (child instanceof DrawioReader r) {
                            stepDto.setReader(ReaderDto.builder().name(r.getName()).type(r.getReaderType()).build());
                        } else if (child instanceof DrawioProcessor p) {
                            stepDto.setProcessor(ProcessorDto.builder().name(p.getName()).type(p.getProcessorType()).build());
                        } else if (child instanceof DrawioWriter w) {
                            stepDto.setWriter(WriterDto.builder().name(w.getName()).type(w.getWriterType()).build());
                        } else if (child instanceof DrawioListeners l) {
                            String containerId = l.getId();
                            components.stream()
                                    .filter(c -> containerId.equals(c.getParentId()))
                                    .forEach(listener -> stepDto.getListeners().add(
                                            ListenerDto.builder().name(listener.getName()).type(listener.getType()).build()
                                    ));
                        }
                    }
                }
            }
        }
    }
}
