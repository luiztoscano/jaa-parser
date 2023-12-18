package com.accenture.parser.custom;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityInfo {
    private String name;
    private String packageName;
    private String owner;
    private String objectName;
    private String objectType;
}
