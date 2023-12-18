package com.accenture.parser;

import com.accenture.parser.csv.CsvReader;
import com.accenture.parser.custom.CustomParser;
import com.accenture.parser.custom.ObjectInfo;
import org.apache.commons.cli.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class App implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Options options = new Options();

		Option inputOption = Option.builder().option("i").longOpt("input").hasArg(true).desc("Input directory").required(true).valueSeparator('=').build();
		Option outputOption = Option.builder().option("o").longOpt("output").hasArg(true).desc("Output directory").required(false).valueSeparator('=').build();
		Option domainOption = Option.builder().option("s").longOpt("schemas").hasArg(true).desc("Schemas (separated by comma)").required(true).valueSeparator('=').build();
		Option dbObjectsOption = Option.builder().option("d").longOpt("dbobjects").hasArg(true).desc("Database objects").required(true).valueSeparator('=').build();

		options.addOption(inputOption);
		options.addOption(outputOption);
		options.addOption(domainOption);
		options.addOption(dbObjectsOption);

		CommandLineParser clp = new DefaultParser();
		CommandLine cl = clp.parse(options, args);

		String input = cl.getOptionValue("i");
		String output = cl.getOptionValue("o");
		String schemas = cl.getOptionValue("s");
		String dbObjects = cl.getOptionValue("d");

		CustomParser parser = new CustomParser();
		parser.parseAll(input, output, schemas, dbObjects);
	}
}
