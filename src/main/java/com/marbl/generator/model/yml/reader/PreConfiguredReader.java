package com.marbl.generator.model.yml.reader;

import lombok.Getter;

import java.util.Map;

@Getter
public enum PreConfiguredReader {

    FLAT_FILE_ITEM_READER(Map.of(
            "resource", "",
            "lineToSkip", 0,
            "delimiter", ";",
            "fieldNames", "",
            "mappedClass", ""
    )),

    JDBC_CURSOR_ITEM_READER(Map.of(
            "datasource", "",
            "preparedStatementClass", "",
            "mappedClass", "",
            "sql", ""
    )),

    JDBC_PAGING_ITEM_READER(Map.of(
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

    public static Map<String, Object> getConfigByType(String type) {
        for (PreConfiguredReader reader : PreConfiguredReader.values()) {
            if (reader.name().equalsIgnoreCase(type)) {
                return reader.getConfig();
            }
        }
       return null;
    }
}

