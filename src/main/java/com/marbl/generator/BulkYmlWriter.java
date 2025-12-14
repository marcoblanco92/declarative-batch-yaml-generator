package com.marbl.generator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.marbl.generator.model.yml.RootYml;
import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@UtilityClass
public class BulkYmlWriter {

    private final ObjectMapper yamlMapper;

    static {
        // Initialize YAML ObjectMapper with custom settings
        yamlMapper = new ObjectMapper(
                new YAMLFactory()
                        .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER) // Disable '---' document start marker
                        .enable(YAMLGenerator.Feature.MINIMIZE_QUOTES) // Minimize quotes where possible
        );

        // Register JavaTimeModule to support java.time types
        yamlMapper.registerModule(new JavaTimeModule());

        // Exclude null values from serialization
        yamlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Write dates as ISO strings instead of timestamps
        yamlMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Writes the RootYml object to a YAML file at the specified path.
     *
     * @param root       The RootYml object to serialize.
     * @param outputPath The path of the output YAML file.
     * @throws IOException If writing to the file fails.
     */
    public void write(RootYml root, Path outputPath) throws IOException {
        yamlMapper.writeValue(outputPath.toFile(), root);
    }

    /**
     * Convenience overload to write the RootYml object to a File.
     *
     * @param root       The RootYml object to serialize.
     * @param outputFile The output YAML file.
     * @throws IOException If writing to the file fails.
     */
    public void write(RootYml root, File outputFile) throws IOException {
        write(root, outputFile.toPath());
    }

    /**
     * Serializes the RootYml object to a YAML string.
     *
     * @param root The RootYml object to serialize.
     * @return YAML string representation of the object.
     * @throws IOException If serialization fails.
     */
    public String writeAsString(RootYml root) throws IOException {
        return yamlMapper.writeValueAsString(root);
    }
}
