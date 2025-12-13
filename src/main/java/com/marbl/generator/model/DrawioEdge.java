package com.marbl.generator.model;

import com.marbl.generator.enums.EdgeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawioEdge {

    private String id;

    /**
     * Next | SimpleFlow | OnCondition
     */
    private EdgeType type;

    private String sourceId;
    private String targetId;

    /**
     * solo per OnCondition
     */
    private String condition;
}
