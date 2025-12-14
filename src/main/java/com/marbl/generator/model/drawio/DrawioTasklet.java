package com.marbl.generator.model.drawio;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DrawioTasklet extends DrawioComponent {
    private Integer order;
}