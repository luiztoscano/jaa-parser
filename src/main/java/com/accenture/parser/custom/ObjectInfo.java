package com.accenture.parser.custom;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class ObjectInfo {
    private String owner;
    private String name;
    private String type;
}
