package com.accenture.parser.custom;

import lombok.Builder;
import lombok.Data;

@Data
//@NoArgsConstructor
@Builder(toBuilder = true)
public class MethodInfo {
    private String owner;
    private String objectName;
    private String objectType;
    private String methodName;
    private String line;
    private Integer lineNumber;
    private String filePath;
    private String fileName;
    private String refOwner;
    private String refObjectName;
    private String refObjectType;
    private String refMethodName;
    private String operation;
}
