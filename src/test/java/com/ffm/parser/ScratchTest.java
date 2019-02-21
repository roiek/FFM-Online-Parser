/*
 * Created on 19-02-2019 22:39 by trojek
 *
 * Copyright (c) 2001-2019 Unity S.A.
 * ul. Przedmiejska 6-10, 54-201 Wrocław, Poland
 * Wszelkie prawa zastrzeżone
 *
 * Niniejsze oprogramowanie jest własnością Unity S.A.
 * Wykorzystanie niniejszego oprogramowania jest możliwe tylko na podstawie
 * i w zgodzie z warunkami umowy licencyjnej zawartej z Unity S.A.
 */

package com.ffm.parser;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.junit.Test;
import org.mozilla.universalchardet.UniversalDetector;


public class ScratchTest {

	@Test
	public void shouldReadCsv() throws IOException {

		// given

		// when


		var bytes = Files.readAllBytes(Paths.get("src/test/resources/Workbook2.csv"));
		UniversalDetector charsetDetector = new UniversalDetector();
		charsetDetector.handleData(bytes);
		charsetDetector.dataEnd();

		var detectedCharset = Charset.forName(charsetDetector.getDetectedCharset());
		System.out.println(detectedCharset);
		System.out.println(new String(bytes, detectedCharset));

		var csvParser = CSVParser.parse(Paths.get("src/test/resources/Workbook2.csv"), detectedCharset, CSVFormat.DEFAULT.withFirstRecordAsHeader());
		final var header = csvParser.getHeaderMap().entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue)).map(Map.Entry::getKey).peek(System.out::println).collect(toList());

		System.out.println(header);
		// then

	}

	@Test
	public void shouldReadPrn() throws IOException {

		// given

		// when
		var bytes = Files.readAllBytes(Paths.get("src/test/resources/Workbook2.prn"));
		UniversalDetector charsetDetector = new UniversalDetector();
		charsetDetector.handleData(bytes);
		charsetDetector.dataEnd();

		var detectedCharset = Charset.forName(charsetDetector.getDetectedCharset());
		System.out.println(new String(bytes, detectedCharset));

		// then

	}
}
