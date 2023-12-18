package com.accenture.parser.csv;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
public class CsvWriter<T> {
    private final Integer BUFFER_SIZE = 1024;
    private StatefulBeanToCsv<T> writer;

    public CsvWriter(Path output, Class<T> clazz) throws IOException {
        log.info("Writing results to {}", output.toString());
        BufferedWriter bw = new BufferedWriter(new FileWriter(output.toFile(), true), BUFFER_SIZE);
        HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
        strategy.setType(clazz);

        this.writer = new StatefulBeanToCsvBuilder<T>(bw)
                .withMappingStrategy(strategy)
                .withApplyQuotesToAll(true)
                .withSeparator(';')
                .build();
    }

    public void write(List<T> entries) {
        try {
            writer.write(entries);
        }
        catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error(e.getMessage(), e);
        }
//        try (BufferedWriter bw = new BufferedWriter(new FileWriter(output.toFile(), true))) {
//            HeaderColumnNameMappingStrategy<TokenInfo> strategy = new HeaderColumnNameMappingStrategy<>();
//
//            strategy.setType(TokenInfo.class);
//
//            StatefulBeanToCsv<TokenInfo> beanWriter = new StatefulBeanToCsvBuilder<TokenInfo>(bw)
//                    .withMappingStrategy(strategy)
//                    .withApplyQuotesToAll(true)
//                    .withSeparator(';')
//                    .build();
//
//            beanWriter.write(tokens);
//        }
//        catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
//            log.error(e.getMessage(), e);
//        }
    }
}
