package com.marbl.generator.model.yml.writer;

import java.util.Map;

public enum PreConfiguredWriter {

    FLATFILEITEMWRITER(Map.of(
            "resource", "",
            "delimiter", ",",
            "lineToSkip", 0,
            "fileHeader", "",
            "fileFooter", "",
            "fieldNames", new String[]{},
            "mappedClass", ""
    )),

    JDBCBATCHITEMWRITER(Map.of(
            "datasource", "",
            "sql", "",
            "preparedStatementClass", "",
            "mappedClass", ""
    ));

    private final Map<String, Object> config;

    PreConfiguredWriter(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public static Map<String, Object> getConfigByType(String type) {
        for (PreConfiguredWriter writer : PreConfiguredWriter.values()) {
            if (writer.name().equalsIgnoreCase(type)) {
                return writer.getConfig();
            }
        }
        return null;
    }
}
