package com.accenture.parser.custom;

import com.accenture.parser.Java20Parser;
import com.accenture.parser.csv.CsvReader;
import com.accenture.parser.csv.CsvWriter;
import com.accenture.parser.grammar.Java20Lexer;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class CustomParser {
    private final String[] EXTENSIONS = { ".java" };
    private List<EntityInfo> parseEntities(Path file) {
        try {
            log.info("Parsing {}", file.toString());
            Java20Lexer lexer = new Java20Lexer(CharStreams.fromFileName(file.toString()));
            Java20Parser parser = new Java20Parser(new CommonTokenStream(lexer));
            ParseTree tree = parser.compilationUnit();
            log.debug(tree.toStringTree(parser));
            CustomEntityListener listener = new CustomEntityListener();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);

            return listener.getEntities();
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);

            return new ArrayList<>();
        }
    }

    private List<VariableInfo> parseVariables(List<EntityInfo> entities, Path file) {
        try {
            log.info("Parsing {}", file.toString());
            Java20Lexer lexer = new Java20Lexer(CharStreams.fromFileName(file.toString()));
            Java20Parser parser = new Java20Parser(new CommonTokenStream(lexer));
            ParseTree tree = parser.compilationUnit();
            log.debug(tree.toStringTree(parser));
            CustomVariableListener listener = new CustomVariableListener(entities);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);

            return listener.getVariables();
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);

            return new ArrayList<>();
        }
    }

    private List<MethodInfo> parseMethods(List<VariableInfo> variables, Path file) {
        try {
            log.info("Parsing {}", file.toString());
            Java20Lexer lexer = new Java20Lexer(CharStreams.fromFileName(file.toString()));
            Java20Parser parser = new Java20Parser(new CommonTokenStream(lexer));
            ParseTree tree = parser.compilationUnit();
            log.debug(tree.toStringTree(parser));
            CustomMethodListener listener = new CustomMethodListener(variables);
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(listener, tree);

            return listener.getMethods();
        }
        catch (IOException e) {
            log.error(e.getMessage(), e);

            return new ArrayList<>();
        }
    }

    private List<String> parseSchemas(String schemas) {
        return Arrays.stream(schemas.split(",")).toList();
    }

    private Map<String, Map<String, ObjectInfo>> parseDbObjects(String objects) throws FileNotFoundException {
        CsvReader<ObjectInfo> reader = new CsvReader<>(Path.of(objects));

        return reader.getEntries().stream().collect(Collectors.groupingBy(ObjectInfo::getOwner,
                Collectors.toMap(o -> {return o.getName() + o.getType();}, Function.identity())));
    }

    public void parseAll(String i, String o, String s, String d) throws IOException {
        Path input = Path.of(i);
        Path output = Path.of(o);
        Map<String, Map<String, ObjectInfo>> dbObjects = parseDbObjects(d);
        List<String> schemas = parseSchemas(s);

        CsvWriter<MethodInfo> writer = new CsvWriter<>(output, MethodInfo.class);

        List<EntityInfo> entities;
        List<VariableInfo> variables;

        try (Stream<Path> walk = Files.walk(input)) {
            entities = new ArrayList<>(walk.filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> Arrays.stream(EXTENSIONS).anyMatch(f::endsWith))
                    .map(f -> parseEntities(Path.of(f)))
                    .flatMap(Collection::stream)
                    .toList());
        }

        try (Stream<Path> walk = Files.walk(input)) {
            variables = new ArrayList<>(walk.filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> Arrays.stream(EXTENSIONS).anyMatch(f::endsWith))
                    .map(f -> parseVariables(entities, Path.of(f)))
                    .flatMap(Collection::stream)
                    .toList());
        }

        try (Stream<Path> walk = Files.walk(input)) {
            walk.filter(p -> !Files.isDirectory(p))
                    .map(p -> p.toString().toLowerCase())
                    .filter(f -> Arrays.stream(EXTENSIONS).anyMatch(f::endsWith))
                    .map(f -> parseMethods(variables, Path.of(f)))
                    .forEach(writer::write);
        }
    }
}
