package com.marbl.generator.mapper.dto.component;

import com.marbl.generator.model.mapper.*;
import com.marbl.generator.model.yml.*;
import com.marbl.generator.model.yml.reader.PreConfiguredReader;
import com.marbl.generator.model.yml.writer.PreConfiguredWriter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Maps child components: Reader, Processor, Writer, Listeners, DataSources.
 */
public class ChildYmlMapper {

    public static ReaderYml mapReader(ReaderDto dto) {
        if (dto == null) return null;
        return ReaderYml.builder()
                .name(dto.getName())
                .type(dto.getType())
                .config(PreConfiguredReader.getConfigByType(dto.getType()))
                .build();
    }

    public static ProcessorYml mapProcessor(ProcessorDto dto) {
        if (dto == null) return null;
        return ProcessorYml.builder()
                .name(dto.getName())
                .type(dto.getType())
                .build();
    }

    public static WriterYml mapWriter(WriterDto dto) {
        if (dto == null) return null;
        return WriterYml.builder()
                .name(dto.getName())
                .type(dto.getType())
                .config(PreConfiguredWriter.getConfigByType(dto.getType()))
                .build();
    }

    public static List<ListenerYml> mapStepListeners(StepDto dto) {
        if (dto.getListeners() == null) return null;
        return dto.getListeners().stream()
                .map(l -> ListenerYml.builder().name(l.getName()).type(l.getType()).build())
                .toList();
    }

    public static ListenerYml mapJobListener(JobDto dto) {
        if (dto.getListener() == null) return null;
        return ListenerYml.builder()
                .name(dto.getListener().getName())
                .type(dto.getListener().getType())
                .build();
    }

    public static DataSourceYml mapDataSource(DataSourceDto dto) {
        return DataSourceYml.builder()
                .name(dto.getName())
                .main(dto.isMain())
                .url("")
                .username("")
                .password("")
                .type(dto.getType())
                .driverClassName("")
                .build();
    }
}
