package com.marbl.generator.model.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JdbcPropertiesYml {

    private String platform;

    @JsonProperty("initialize-schema")
    private String initializeSchema;

    @JsonProperty("table-prefix")
    private String tablePrefix;
}
