package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.yml.LoggingPatternYml;
import com.marbl.generator.model.yml.LoggingYml;
import java.util.Map;

/**
 * Creates logging configuration (LoggingYml).
 */
public class LoggingYmlMapper {

    public static LoggingYml mapLogging(String basePackage) {
        return LoggingYml.builder()
                .level(Map.of(
                        "root", "INFO",
                        basePackage, "DEBUG"
                ))
                .pattern(LoggingPatternYml.builder()
                        .console(defaultConsolePattern())
                        .build())
                .build();
    }

    private static String defaultConsolePattern() {
        return "[%d{dd-MM-yyyy HH:mm:ss.SSSS}][%-4thread][%-0X{stepName}][%-0X{chunk}][%-3level][%-20logger{1}] %msg%n";
    }
}
