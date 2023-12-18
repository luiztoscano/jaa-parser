package com.accenture.parser.custom;

import com.accenture.parser.Java20Parser;
import com.accenture.parser.Java20ParserBaseListener;
import lombok.Getter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomVariableListener extends Java20ParserBaseListener {

    private final static String[] MODIFY_METHODS = {"persist", "save"};

    private final Map<String, EntityInfo> entities;

    @Getter
    private Map<String, MethodInfo> methods = new HashMap<>();

    private Map<String, VariableInfo> variables = new HashMap<>();

    private Map<String, String> imports = new HashMap<>();

    private VariableInfo variable;

    public CustomVariableListener(List<EntityInfo> entities) {
        this.entities = entities.stream().
                collect(Collectors.toMap(e -> {return e.getPackageName() + "." + e.getName();}, Function.identity()));
    }

    @Override
    public void enterSingleTypeImportDeclaration(Java20Parser.SingleTypeImportDeclarationContext ctx) {
        String declaration = ctx.getChild(0).getText();
        String clazz = declaration.substring(declaration.lastIndexOf(".") + 1);
        this.imports.put(clazz, declaration);

        super.enterSingleTypeImportDeclaration(ctx);
    }

    @Override
    public void enterTypeVariable(Java20Parser.TypeVariableContext ctx) {
        String clazz = imports.get(ctx.getChild(0).getText());

        if (!Objects.isNull(clazz)) {
            EntityInfo entity = entities.get(clazz);

            if (!Objects.isNull(entity)) {
                variable = VariableInfo.builder().clazz(clazz).build();
            }
        }

        super.enterTypeVariable(ctx);
    }

    @Override
    public void enterVariableDeclaratorId(Java20Parser.VariableDeclaratorIdContext ctx) {
        if (!Objects.isNull(variable)) {
            variable.setName(ctx.getChild(0).getText());
            variables.put(variable.getName(), variable);
            variable = null;
        }
        super.enterVariableDeclaratorId(ctx);
    }

    @Override
    public void enterMethodInvocation(Java20Parser.MethodInvocationContext ctx) {
        String method = ctx.getChild(0).getText();

        super.enterMethodInvocation(ctx);
    }

    @Override
    public void exitMethodInvocation(Java20Parser.MethodInvocationContext ctx) {
        super.exitMethodInvocation(ctx);
    }
}
