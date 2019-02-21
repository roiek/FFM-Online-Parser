/*
 * Created on 21-02-2019 22:51 by trojek
 *
 * Copyright (c) 2001-2019 Unity S.A.
 * ul. Przedmiejska 6-10, 54-201 Wrocław, Poland
 * Wszelkie prawa zastrzeżone
 *
 * Niniejsze oprogramowanie jest własnością Unity S.A.
 * Wykorzystanie niniejszego oprogramowania jest możliwe tylko na podstawie
 * i w zgodzie z warunkami umowy licencyjnej zawartej z Unity S.A.
 */

package com.ffm.parser.fileparser;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toUnmodifiableList;
import static org.apache.commons.csv.CSVFormat.DEFAULT;
import static org.apache.logging.log4j.util.Strings.EMPTY;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;


@Service
public class CsvFileParser implements FileParsingStrategy {

	private static final String CSV_EXTENSION = "csv";

	@Override
	public String supportedExtension() {

		return CSV_EXTENSION;
	}

	@Override
	public ParsedFileResponseDto parse(File file, Charset charset) throws IOException {

		final CSVParser csvData = CSVParser.parse(file, charset, DEFAULT.withFirstRecordAsHeader());

		final var header = parseHeader(csvData);
		final var records = parseRecords(csvData, header);

		return new ParsedFileResponseDto(header, records);
	}

	private Row parseHeader(CSVParser csvParser) {

		final List<String> header = csvParser.getHeaderMap().entrySet().stream()
			.sorted(comparing(Map.Entry::getValue))
			.map(Map.Entry::getKey)
			.collect(toList());

		return new Row(header);
	}

	private List<Row> parseRecords(CSVParser csvParser, Row header) throws IOException {

		return csvParser.getRecords().stream()
			.map(record -> toValuesList(record, header.size()))
			.map(Row::new)
			.collect(toList());
	}

	private List<String> toValuesList(CSVRecord strings, int expectedSize) {

		return IntStream.range(0, expectedSize)
			.mapToObj(getOrEmpty(strings))
			.collect(toUnmodifiableList());
	}

	private IntFunction<String> getOrEmpty(CSVRecord record) {

		return index -> {

			if (index < record.size()) {
				return record.get(index);
			}

			return EMPTY;
		};
	}
}
