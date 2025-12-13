package com.marbl.generator.model.mapper;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private String name;
    private List<Step> steps = new ArrayList<>();
    private ListenerDto listener;
}
