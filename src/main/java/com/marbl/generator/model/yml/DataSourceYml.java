package com.marbl.generator.model.yml;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceYml {

    private String name;

    private Boolean main;

    private String url;

    private String username;

    private String password;

    private String type;

    @JsonProperty("driver-class-name")
    private String driverClassName;

}
