package com.accenture.parser.custom;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariableInfo {
    private String name;
    private String clazz;
}
