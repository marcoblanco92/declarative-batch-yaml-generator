package com.marbl.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ParsedDrawio {
    private List<DrawioComponent> components;
    private List<DrawioEdge> edges;
}
