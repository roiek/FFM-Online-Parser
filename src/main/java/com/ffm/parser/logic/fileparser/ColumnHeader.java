/*
 * Created on 23-02-2019 19:03 by trojek
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

import java.util.StringJoiner;
import java.util.stream.Stream;

import org.springframework.util.Assert;


class ColumnHeader {

	private final int startIndex;
	private final int endIndex;
	private final String value;

	ColumnHeader(int startIndex, int endIndex, String value) {

		Assert.state(startIndex < endIndex, String.format("startIndex must be smaller then endIndex, got startIndex: %s endIndex: %s", startIndex, endIndex));
		Assert.hasText(value, "value must not be null or empty");

		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.value = value;
	}

	boolean containsWhitespace() {

		return value.contains(" ");
	}

	String getValue() {

		return value;
	}

	int getStartIndex() {

		return startIndex;
	}

	int getEndIndex() {

		return endIndex;
	}

	Stream<ColumnHeader> splitAt(int index) {

		return Stream.of(
			new ColumnHeader(startIndex, startIndex + index, value.substring(0, index).trim()),
			new ColumnHeader(startIndex + index, endIndex, value.substring(index).trim())
		);
	}

	@Override
	public String toString() {

		return new StringJoiner(", ", ColumnHeader.class.getSimpleName() + "[", "]")
			.add("startIndex=" + startIndex)
			.add("endIndex=" + endIndex)
			.add("value='" + value + "'")
			.toString();
	}
}
