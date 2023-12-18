package com.accenture.parser.custom;

import com.accenture.parser.Java20Parser;
import com.accenture.parser.Java20ParserBaseListener;
import lombok.Getter;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CustomEntityListener extends Java20ParserBaseListener {
    private static final String[] ANNOTATIONS = {"Entity", "Table"};

    @Getter
    private List<EntityInfo> entities;

    private EntityInfo entity;

    private Boolean isEntityAnnotation(String name) {
        return Arrays.stream(ANNOTATIONS).anyMatch(name::equalsIgnoreCase);
    }

    @Override
    public void enterPackageName(Java20Parser.PackageNameContext ctx) {
        entity = EntityInfo.builder().packageName(ctx.getChild(0).getText()).build();

        super.enterPackageName(ctx);
    }

    @Override
    public void enterAnnotationInterfaceDeclaration(Java20Parser.AnnotationInterfaceDeclarationContext ctx) {
        String annotationName = ctx.getChild(0).getText();

        if (isEntityAnnotation(annotationName)) {
            entity.setName(annotationName);
            entities.add(entity);
        }

        super.enterAnnotationInterfaceDeclaration(ctx);
    }
}
