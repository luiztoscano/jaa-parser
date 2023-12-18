package com.accenture.parser.custom;

import com.accenture.parser.Java20ParserBaseListener;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomMethodListener extends Java20ParserBaseListener {


    private final Map<String, VariableInfo> variables;

    @Getter
    private List<MethodInfo> methods = new ArrayList<>();

    private MethodInfo method;

    public CustomMethodListener(List<VariableInfo> variables) {
        this.variables = variables.stream()
                .collect(Collectors.toMap(VariableInfo::getName, Function.identity()));
    }
}
