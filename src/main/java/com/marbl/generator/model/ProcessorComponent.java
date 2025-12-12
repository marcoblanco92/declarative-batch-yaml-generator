package com.marbl.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcessorComponent extends DrawioComponent {
    private String processorType;
}