package com.marbl.generator.model;

import com.marbl.generator.dto.DrawioComponent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BulkYml {

    private JobYml job;
    private List<DataSourceYml> dataSources;
}
