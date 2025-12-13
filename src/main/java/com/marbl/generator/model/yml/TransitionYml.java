package com.marbl.generator.model.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransitionYml {

    private String from;

    @JsonProperty("on-condition")
    private String onCondition;

    @JsonProperty("to-step")
    private String toStep;
}
