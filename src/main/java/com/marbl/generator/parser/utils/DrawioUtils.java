package com.marbl.generator.parser.utils;

import com.marbl.generator.enums.EdgeType;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DrawioUtils {

    public boolean isEdgeSource(String source) {
        return "Next".equals(source) || "SimpleFlow".equals(source) || "OnCondition".equals(source);
    }

    public EdgeType edgeTypeFromSource(String source) {
        return switch (source) {
            case "Next" -> EdgeType.NEXT;
            case "SimpleFlow" -> EdgeType.SIMPLE_FLOW;
            case "OnCondition" -> EdgeType.ON_CONDITION;
            default -> null;
        };
    }
}
