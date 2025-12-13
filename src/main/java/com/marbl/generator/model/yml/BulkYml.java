package com.marbl.generator.model.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class BulkYml {

    @JsonProperty("batch-properties")
    private BatchPropertiesYml batchProperties;

    private Map<String, DataSourceYml> datasources;

    @JsonProperty("batch-job")
    private JobYml batchJob;
}
