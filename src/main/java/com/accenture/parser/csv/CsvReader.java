package com.accenture.parser.csv;

import com.accenture.parser.custom.ObjectInfo;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.Getter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.util.List;

public class CsvReader<T> {
    @Getter
    private final List<T> entries;

    public CsvReader(Path input) throws FileNotFoundException {
        CSVReader reader = new CSVReader(new FileReader(input.toFile()));
        this.entries = new CsvToBeanBuilder<T>(reader).build().parse();
    }
}
