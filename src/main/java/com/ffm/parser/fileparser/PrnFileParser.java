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

import java.io.File;
import java.nio.charset.Charset;

import org.springframework.stereotype.Service;


@Service
public class PrnFileParser implements FileParsingStrategy {

	private static final String PRN_EXTENSION = "prn";

	@Override
	public String supportedExtension() {

		return PRN_EXTENSION;
	}

	@Override
	public ParsedFileResponseDto parse(File file, Charset charset) {

		return null;
	}
}
