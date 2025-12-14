package com.marbl.generator.model.drawio;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DrawioStep extends DrawioComponent {
    private Integer chunkSize;
    private Integer order;
}