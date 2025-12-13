package com.marbl.generator.model.drawio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DrawioComponent {
    private String id;
    private String source;
    private String name;
    private String type;
    private String parentId;
}