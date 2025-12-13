package com.marbl.generator.model.drawio;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DrawioParsed {
    private List<DrawioComponent> components;
    private List<DrawioEdge> edges;
}
