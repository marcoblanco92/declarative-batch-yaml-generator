package com.marbl.generator.model;


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
public class JobYml {
    private String name;
    private List<Step> steps = new ArrayList<>();
    private ListenerYml listener;
}
