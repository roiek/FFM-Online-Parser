/*
 * Created on 21-02-2019 23:31 by trojek
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

import static java.nio.file.Files.createTempFile;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class CsvFileParserTest {

	private CsvFileParser csvFileParser;

	private Path tempFilePath;

	@Before
	public void setUp() {

		this.csvFileParser = new CsvFileParser();
	}

	@Test
	public void shouldTest() throws IOException {

		//given
		var data = "Name,Address,Postcode,Phone,Credit Limit,Birthday\n"
			+ "\"Johnson, John\",Voorstraat 32,3122gg,020 3849381,10000,01/01/1987\n"
			+ "\"Anderson, Paul\",Dorpsplein 3A,4532 AA,030 3458986,109093,03/12/1965";

		tempFilePath = createTempFile("test", ".csv");
		Files.write(tempFilePath, data.getBytes());

		//when
		var result = csvFileParser.parse(tempFilePath.toFile(), Charset.forName("windows-1252"));

		//then
		assertThat(result.getHeaderRow().getData()).containsExactly("Name", "Address", "Postcode", "Phone", "Credit Limit", "Birthday");
		assertThat(result.getDataRows()).containsExactly(
			new Row(List.of("Johnson, John","Voorstraat 32","3122gg","020 3849381","10000","01/01/1987")),
			new Row(List.of("Anderson, Paul","Dorpsplein 3A","4532 AA","030 3458986","109093","03/12/1965"))
		);
	}

	@After
	public void teardown() {
		tempFilePath.toFile().delete();
	}
}