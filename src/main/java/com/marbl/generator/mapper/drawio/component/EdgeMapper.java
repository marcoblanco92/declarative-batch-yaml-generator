package com.marbl.generator.mapper.drawio.component;

import com.marbl.generator.model.drawio.*;
import com.marbl.generator.model.mapper.*;
import java.util.*;

/**
 * Maps DrawioEdge objects to step transitions (nextStep / conditionalNext).
 */
public class EdgeMapper {

    public static void mapEdges(List<DrawioEdge> edges, Map<String, Step> stepsById, List<DrawioComponent> components) {
        Map<String, DrawioComponent> componentById = new HashMap<>();
        components.forEach(c -> componentById.put(c.getId(), c));

        for (DrawioEdge edge : edges) {
            DrawioComponent sourceComp = componentById.get(edge.getSourceId());
            DrawioComponent targetComp = componentById.get(edge.getTargetId());
            if (sourceComp == null || targetComp == null) continue;

            Step sourceStep = getParentStep(sourceComp, stepsById);
            Step targetStep = getParentStep(targetComp, stepsById);
            if (sourceStep == null || targetStep == null) continue;

            switch (edge.getType()) {
                case NEXT -> sourceStep.setNextStep(targetStep.getName());
                case ON_CONDITION -> {
                    String key = edge.getCondition() != null ? edge.getCondition() : "DEFAULT";
                    targetStep.getConditionalNext().put(key, sourceStep.getName());
                }
                default -> {}
            }
        }
    }

    private static Step getParentStep(DrawioComponent comp, Map<String, Step> stepsById) {
        if (comp instanceof DrawioStep || comp instanceof DrawioTasklet) {
            return stepsById.get(comp.getId());
        } else if (comp.getParentId() != null) {
            return stepsById.get(comp.getParentId());
        }
        return null;
    }
}
