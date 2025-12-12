package com.marbl.generator.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WriterComponent extends DrawioComponent {
    private String writerType;
}