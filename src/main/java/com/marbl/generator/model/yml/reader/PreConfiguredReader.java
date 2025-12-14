package com.marbl.generator.model.yml.reader;

import java.util.Map;

public enum PreConfiguredReader {

    FLATFILERITEMEADER(Map.of(
            "resource", "",
            "lineToSkip", 0,
            "delimiter", ";",
            "fieldNames", "",
            "mappedClass", ""
    )),

    JDBCCURSORITEMREADER(Map.of(
            "datasource", "",
            "preparedStatementClass", "",
            "mappedClass", "",
            "sql", ""
    )),

    JDBCPAGINGITEMREADER(Map.of(
            "datasource", "",
            "preparedStatementClass", "",
            "providerType", "",      // enum type, null di default
            "mappedClass", "",
            "clause", "",            // PagingSqlModel
            "parameters", Map.of()     // Map vuota come default
    ));

    private final Map<String, Object> config;

    PreConfiguredReader(Map<String, Object> config) {
        this.config = config;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public static Map<String, Object> getConfigByType(String type) {
        for (PreConfiguredReader reader : PreConfiguredReader.values()) {
            if (reader.name().equalsIgnoreCase(type)) {
                return reader.getConfig();
            }
        }
       return null;
    }
}

