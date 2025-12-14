package com.marbl.generator.model.yml;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class TaskletYml implements StepYmlBase {

    private String name;

    private String type; // TASKLET

    private String tasklet;

    private String next;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<TransitionYml> transitions;
}
