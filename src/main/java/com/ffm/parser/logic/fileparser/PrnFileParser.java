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

package com.ffm.parser.logic.fileparser;

import static java.lang.Math.min;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static org.apache.commons.io.FileUtils.readLines;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;


@Service
public class PrnFileParser implements FileParsingStrategy {

	private static final String PRN_EXTENSION = "prn";
	public static final String TWO_OR_MORE_WHITESPACES_REGEXP = "[A-Za-z]*\\s{2,}";
	public static final Pattern TWO_OR_MORE_WHITESPACES_PATTERN = compile(TWO_OR_MORE_WHITESPACES_REGEXP);
	public static final Pattern ONE_WHITESPACE_PATTERN = compile("\\s");

	@Override
	public String supportedExtension() {


		return PRN_EXTENSION;
	}

	@Override
	public ParsedFileResponseDto parse(File file, Charset charset) throws IOException {

		final List<String> lines = readLines(file, charset);

		final var columnHeaders = parseHeader(lines);
		final var headerRow = new Row(columnHeaders.stream().map(ColumnHeader::getValue).collect(toList()));
		final var records = parseRecords(lines, columnHeaders);

		return new ParsedFileResponseDto(headerRow, records, charset);
	}

	private List<ColumnHeader> parseHeader(List<String> lines) {

		var headerLine = lines.get(0);

		var potentialHeaders = findPotentialHeaders(headerLine);

		return potentialHeaders.stream()
			.flatMap(potentialHeaderColumn -> trySplit(potentialHeaderColumn, lines))
			.collect(toList());
	}

	private List<Row> parseRecords(List<String> lines, List<ColumnHeader> headerRow) {

		return lines.stream()
			.skip(1)
			.map(parseSingleRecord(headerRow))
			.collect(toList());
	}

	private Function<String, Row> parseSingleRecord(List<ColumnHeader> headerRow) {

		return line -> new Row(headerRow.stream()
			.map(header -> line.substring(header.getStartIndex(), findDataRowEndIndex(header, line.length())))
			.map(String::trim)
			.collect(toList())
		);
	}

	private int findDataRowEndIndex(ColumnHeader header, int rowLength) {

		return min(header.getEndIndex(), rowLength);
	}

	private List<ColumnHeader> findPotentialHeaders(String data) {

		final List<Integer> indexes = new ArrayList<>();
		final var matcher = TWO_OR_MORE_WHITESPACES_PATTERN.matcher(data);

		indexes.add(0);
		while (matcher.find()) {
			indexes.add(matcher.end());
		}
		indexes.add(data.length());

		return range(1, indexes.size())
			.mapToObj(i -> new ColumnHeader(indexes.get(i-1), indexes.get(i), data.substring(indexes.get(i-1), indexes.get(i)).trim()))
			.collect(toList());
	}

	private Stream<ColumnHeader> trySplit(ColumnHeader potentialHeader, List<String> lines) {

		if (potentialHeader.containsWhitespace()) {

			final Matcher matcher1 = compile(potentialHeader.getValue()).matcher(lines.get(0));
			final int start = matcher1.find() ? matcher1.start() : 0;

			final var matcher = ONE_WHITESPACE_PATTERN.matcher(potentialHeader.getValue());
			while (matcher.find()) {

				if (lines.stream().allMatch(line -> line.charAt(start + matcher.start()) == ' ')) {

					return potentialHeader.splitAt(matcher.start())
						.flatMap(splitHeader -> trySplit(splitHeader, lines));
				}
			}
		}

		return Stream.of(potentialHeader);
	}
}
