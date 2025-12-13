package com.marbl.generator.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReaderYml {
    private String name;
    private String type;     // FlatFileItemReader, JdbcPagingItemReader, ecc.
    private Map<String, String> properties = new HashMap<>();
}