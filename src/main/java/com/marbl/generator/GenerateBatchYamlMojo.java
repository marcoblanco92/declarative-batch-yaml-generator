package com.marbl.generator;


import com.marbl.generator.model.drawio.DrawioParsed;
import com.marbl.generator.model.mapper.BulkDto;
import com.marbl.generator.model.yml.RootYml;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;


import java.io.File;

/**
 * Maven plugin to generate declarative batch YAML from draw.io
 */
@Mojo(name = "generate")
public class GenerateBatchYamlMojo extends AbstractMojo {

    /**
     * Input file (draw.io XML)
     */
    @Parameter(property = "inputFile", required = true)
    private File inputFile;

    /**
     * Output directory for the generated YAML
     */
    @Parameter(property = "outputDir", defaultValue = "${project.build.directory}/generated-resources/batch")
    private File outputDir;

    @Override
    public void execute() throws MojoExecutionException {
        getLog().info("Starting YAML generation...");
        getLog().info("Input file: " + inputFile.getAbsolutePath());
        getLog().info("Output directory: " + outputDir.getAbsolutePath());

        try {
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            DrawioParsed drawioParsed = DrawioDomParser.parse(inputFile);
            BulkDto bulkDto = DrawioToDtoMapper.mapToBulkDto(drawioParsed);
            RootYml rootYml = DtoToYmlMapper.mapToRootYaml(bulkDto);
            BulkYmlWriter.write(rootYml, outputDir);

            getLog().info("YAML generation completed successfully.");
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate YAML", e);
        }
    }
}

