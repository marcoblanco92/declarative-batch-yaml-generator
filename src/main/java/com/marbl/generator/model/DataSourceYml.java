package com.marbl.generator.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DataSourceYml {
    private String name;
    private String type;
    private Map<String, String> properties = new HashMap<>();
    private boolean main;
}
