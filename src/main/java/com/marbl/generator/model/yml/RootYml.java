package com.marbl.generator.model.yml;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RootYml {

    private BulkYml bulk;

    private LoggingYml logging;
}
