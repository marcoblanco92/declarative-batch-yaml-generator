package com.marbl.generator.model.yml.writer;

import lombok.Getter;

import java.util.Map;

import static com.marbl.generator.utils.EnumNameNormalizer.normalize;

@Getter
public enum PreConfiguredWriter {

    FLAT_FILE_ITEM_WRITER(Map.of(
            "resource", "",
            "delimiter", ",",
            "lineToSkip", 0,
            "fileHeader", "",
            "fileFooter", "",
            "fieldNames", new String[]{},
            "mappedClass", ""
    )),

    JDBC_BATCH_ITEM_WRITER(Map.of(
            "datasource", "",
            "sql", "",
            "preparedStatementClass", "",
            "mappedClass", ""
    ));

    private final Map<String, Object> config;

    PreConfiguredWriter(Map<String, Object> config) {
        this.config = config;
    }

    public static Map<String, Object> getConfigByType(String type) {
        if (type == null || type.isBlank()) {
            return null;
        }

        String normalizedType = normalize
                (type);

        for (PreConfiguredWriter writer : PreConfiguredWriter.values()) {
            if (writer.name().equals(normalizedType)) {
                return writer.getConfig();
            }
        }
        return null;
    }
}
