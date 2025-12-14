package com.marbl.generator.model.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@JsonPropertyOrder({
        "batch-properties",
        "datasources",
        "batch-job"
})
@Data
@Builder
public class BulkYml {

    @JsonProperty("batch-properties")
    private BatchPropertiesYml batchProperties;

    private Map<String, DataSourceYml> datasources;

    @JsonProperty("batch-job")
    private JobYml batchJob;
}
