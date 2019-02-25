/*
 * Created on 25-02-2019 18:01 by trojek
 *
 * Copyright (c) 2001-2019 Unity S.A.
 * ul. Przedmiejska 6-10, 54-201 Wrocław, Poland
 * Wszelkie prawa zastrzeżone
 *
 * Niniejsze oprogramowanie jest własnością Unity S.A.
 * Wykorzystanie niniejszego oprogramowania jest możliwe tylko na podstawie
 * i w zgodzie z warunkami umowy licencyjnej zawartej z Unity S.A.
 */

package com.ffm.parser.mvc;

import java.util.List;

import org.springframework.util.Assert;


class ParseFileValidationResult {

	private final List<String> errors;

	private ParseFileValidationResult(List<String> errors) {

		Assert.notNull(errors, "errors must not be null");

		this.errors = List.copyOf(errors);
	}

	static ParseFileValidationResult ok() {

		return new ParseFileValidationResult(List.of());
	}

	static ParseFileValidationResult error(List<String> errors) {

		Assert.notNull(errors, "errors must not be null");
		Assert.notEmpty(errors, "errors must not be an empty list");

		return new ParseFileValidationResult(errors);
	}

	boolean hasErrors() {

		return !errors.isEmpty();
	}

	List<String> getErrors() {

		return errors;
	}
}
