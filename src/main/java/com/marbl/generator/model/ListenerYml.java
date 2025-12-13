package com.marbl.generator.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListenerYml {
    private String name;
    private String type;     // StepExecutionListener, JobExecutionListener
}