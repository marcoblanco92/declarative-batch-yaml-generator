package com.marbl.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marbl.generator.model.yml.BulkYml;
import com.marbl.generator.model.yml.RootYml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class BulkYmlWriter {

    private final ObjectMapper yamlMapper;

    public BulkYmlWriter() {
        this.yamlMapper = new ObjectMapper(
                new YAMLFactory()
                        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER) // niente ---
                        .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES)
        );

        this.yamlMapper.registerModule(new JavaTimeModule());
        this.yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.yamlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Scrive il BulkYml su file YAML con root "bulk"
     */
    public void write(RootYml root, Path outputPath) throws IOException {

        yamlMapper.writeValue(outputPath.toFile(), root);
    }

    /**
     * Overload di comodità
     */
    public void write(RootYml root, File outputFile) throws IOException {
        write(root, outputFile.toPath());
    }

    /**
     * Overload per ottenere lo YAML come String
     */
    public String writeAsString(RootYml root) throws IOException {
        return yamlMapper.writeValueAsString(root);
    }
}
