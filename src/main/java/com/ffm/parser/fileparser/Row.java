/*
 * Created on 21-02-2019 22:30 by trojek
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

import java.util.List;
import java.util.StringJoiner;

import org.springframework.util.Assert;


public class Row {

	private final List<String> data;

	Row(List<String> data) {

		Assert.notNull(data, "data must not be null");

		this.data = data;
	}

	public List<String> getData() {

		return data;
	}

	int size() {

		return data.size();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Row row = (Row) o;

		return data.equals(row.data);
	}

	@Override
	public int hashCode() {

		return data.hashCode();
	}

	@Override
	public String toString() {

		return new StringJoiner(", ", Row.class.getSimpleName() + "[", "]")
			.add("data=" + data)
			.toString();
	}
}
