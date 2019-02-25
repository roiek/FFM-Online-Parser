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

package com.ffm.parser.fileparser;

import org.springframework.util.Assert;


class PotentialHeader {

	private final int startIndex;
	private final int endIndex;
	private final String value;

	public PotentialHeader(int startIndex, int endIndex, String value) {

		Assert.state(startIndex < endIndex, "startIndex must be smaller then endIndex");
		Assert.hasText(value, "value must have text");

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
}
