package com.marbl.generator.model.mapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkDto {

    private JobDto job;
    private List<DataSourceDto> dataSources;
}
