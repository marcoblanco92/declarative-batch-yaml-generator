package com.marbl.generator.model.mapper;


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
public class ReaderDto {
    private String name;
    private String type;     // FlatFileItemReader, JdbcPagingItemReader, ecc.
}