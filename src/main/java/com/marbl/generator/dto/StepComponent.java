package com.marbl.generator.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StepComponent extends DrawioComponent {
    private Integer chunkSize;
}