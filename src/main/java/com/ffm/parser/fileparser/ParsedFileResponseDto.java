/*
 * Created on 21-02-2019 22:29 by trojek
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

import static java.util.stream.Collectors.toList;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.List;
import java.util.StringJoiner;
import java.util.function.Predicate;

import org.springframework.util.Assert;


public class ParsedFileResponseDto {

	private final Row headerRow;
	private final List<Row> dataRows;

	public ParsedFileResponseDto(Row headerRow, List<Row> dataRows) {

		Assert.notNull(headerRow, "headerRow must not be null");
		Assert.notNull(dataRows, "dataRows must not be null");
		assertDataRows(headerRow, dataRows);

		this.headerRow = headerRow;
		this.dataRows = dataRows;
	}

	private void assertDataRows(Row headerRow, List<Row> dataRows) {

		final List<Row> invalidRows = dataRows.stream()
			.filter(sizeNotEqauls(headerRow))
			.collect(toList());

		Assert.state(isEmpty(invalidRows), String.format("all dataRows need to have the same number of columns as header, failing rows contained %s", invalidRows.toString()));
	}

	private Predicate<? super Row> sizeNotEqauls(Row headerRow) {

		return row -> row.size() != headerRow.size();
	}

	public Row getHeaderRow() {

		return headerRow;
	}

	public List<Row> getDataRows() {

		return dataRows;
	}

	@Override
	public String toString() {

		return new StringJoiner(", ", ParsedFileResponseDto.class.getSimpleName() + "[", "]")
			.add("headerRow=" + headerRow)
			.add("dataRows=" + dataRows)
			.toString();
	}
}
