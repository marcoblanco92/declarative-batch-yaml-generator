package com.marbl.generator;


import com.marbl.generator.mapper.drawio.DrawioToDtoMapper;
import com.marbl.generator.mapper.dto.DtoToYmlMapper;
import com.marbl.generator.model.drawio.DrawioParsed;
import com.marbl.generator.model.mapper.BulkDto;
import com.marbl.generator.model.yml.RootYml;
import com.marbl.generator.parser.DrawioDomParser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name = "generate")
public class GenerateBatchYamlMojo extends AbstractMojo {

    @Parameter(property = "inputFile", required = true)
    private File inputFile;

    @Parameter(
            property = "outputDir",
            defaultValue = "${project.build.directory}/generated-resources/batch"
    )
    private File outputDir;

    @Parameter(
            property = "logging.basePackage",
            defaultValue = "${project.groupId}"
    )
    private String loggingBasePackage;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Generating declarative batch YAML from draw.io");

        validateInputFile();
        prepareOutputDir();

        try {
            DrawioParsed drawioParsed = DrawioDomParser.parse(inputFile);
            BulkDto bulkDto = DrawioToDtoMapper.mapToBulkDto(drawioParsed);
            RootYml rootYml = DtoToYmlMapper.mapToRootYaml(bulkDto, loggingBasePackage);

            File outputFile = new File(outputDir, "application-generated.yml");
            getLog().info("Writing YAML to: " + outputFile.getAbsolutePath());

            BulkYmlWriter.write(rootYml, outputFile);

            getLog().info("YAML generated successfully: " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate YAML", e);
        }
    }

    private void validateInputFile() throws MojoExecutionException {
        if (!inputFile.exists() || !inputFile.isFile()) {
            throw new MojoExecutionException(
                    "Input file does not exist or is not a file: " + inputFile.getAbsolutePath()
            );
        }
    }

    private void prepareOutputDir() throws MojoExecutionException {
        if (outputDir.exists() && !outputDir.isDirectory()) {
            throw new MojoExecutionException(
                    "Output path is not a directory: " + outputDir.getAbsolutePath()
            );
        }

        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new MojoExecutionException(
                    "Failed to create output directory: " + outputDir.getAbsolutePath()
            );
        }
    }
}


