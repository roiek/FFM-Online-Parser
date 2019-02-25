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

package com.ffm.parser.logic.fileparser;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PrnFileParserTest {

	private PrnFileParser prnFileParser;

	private Path tempFilePath;

	@Before
	public void setUp() {

		this.prnFileParser = new PrnFileParser();
	}

	@Test
	public void shouldHandleHeaderWithMoreThenOneWhitespace() throws IOException {

		// given
		var header = "Name  Address";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Name", "Address");
	}

	@Test
	public void shouldNotSplitHeaderWithOneWhitespaceWithRightAlignedData() throws IOException {

		// given
		var header = "Credit Limit\n" +
					 "    10909300";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Credit Limit");
		assertThat(result.getDataRows()).hasSize(1);
		assertThat(result.getDataRows().get(0).getData()).containsExactly("10909300");
	}

	@Test
	public void shouldNotSplitHeaderWithOneWhitespaceWithLeftAlignedData() throws IOException {

		// given
		var header = "Credit Limit\n" +
					 "10909300";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Credit Limit");
		assertThat(result.getDataRows()).hasSize(1);
		assertThat(result.getDataRows().get(0).getData()).containsExactly("10909300");
	}

	@Test
	public void shouldSplitHeaderWithOneWhitespaceDependingOnDataInRowsBelow() throws IOException {

		// given
		var header = "Credit Limit Test \n" +
					 "10909300     Test1";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Credit Limit", "Test");
		assertThat(result.getDataRows()).hasSize(1);
		assertThat(result.getDataRows().get(0).getData()).containsExactly("10909300", "Test1");
	}

	@Test
	public void shouldSplitHeaderWithOneWhitespaceDependingOnDataInRowsBelow2() throws IOException {

		// given
		var header = "Credit Limit Test Test\n" +
					 "10909300     Test1    ";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Credit Limit", "Test Test");
		assertThat(result.getDataRows()).hasSize(1);
		assertThat(result.getDataRows().get(0).getData()).containsExactly("10909300", "Test1");
	}

	@Test
	public void shouldHandleMultipleHeaderSplitting() throws IOException {

		// given
		var header = "Credit Limit Test Test  Header\n" +
			  		 "10909300     Test1       Head ";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Credit Limit", "Test Test", "Header");
		assertThat(result.getDataRows()).hasSize(1);
		assertThat(result.getDataRows().get(0).getData()).containsExactly("10909300", "Test1", "Head");
	}

	@Test
	public void shouldHandleMultipleOneWhitespacedHeaderSplitting() throws IOException {

		// given
		var header = "Credit Limit Test Test Header\n" +
					 "10909300     Test1     Head ";
		prepareTestFile(header);

		// when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		// then
		assertThat(result.getHeaderRow().getData()).containsExactly("Credit Limit", "Test Test", "Header");
		assertThat(result.getDataRows()).hasSize(1);
		assertThat(result.getDataRows().get(0).getData()).containsExactly("10909300", "Test1", "Head");
	}

	@Test
	public void shouldTestOnRealData() throws IOException {

		//given
		var data = "Name            Address               Postcode Phone         Credit Limit Birthday\n"
				 + "Johnson, John   Voorstraat 32         3122gg   020 3849381        1000000 19870101\n"
				 + "Anderson, Paul  Dorpsplein 3A         4532 AA  030 3458986       10909300 19651203";

		prepareTestFile(data);

		//when
		var result = prnFileParser.parse(tempFilePath.toFile(), forName("windows-1252"));

		//then
		assertThat(result.getHeaderRow().getData()).containsExactly("Name", "Address", "Postcode", "Phone", "Credit Limit", "Birthday");
		assertThat(result.getDataRows()).containsExactly(
			new Row(List.of("Johnson, John","Voorstraat 32","3122gg","020 3849381","1000000","19870101")),
			new Row(List.of("Anderson, Paul","Dorpsplein 3A","4532 AA","030 3458986","10909300","19651203"))
		);
	}

	private void prepareTestFile(String data) throws IOException {

		tempFilePath = createTempFile("", "test.prn");
		write(tempFilePath, data.getBytes());
	}

	@After
	public void teardown() {
		tempFilePath.toFile().delete();
	}
}