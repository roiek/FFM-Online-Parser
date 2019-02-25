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

import static org.apache.commons.io.FileUtils.deleteQuietly;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class FileParser implements SupportedExtensionsProvider {

	private final CharsetDetector charsetDetector;
	private final List<FileParsingStrategy> strategies;
	private final List<String> supportedExtensions;

	@Autowired
	FileParser(CharsetDetector charsetDetector, List<FileParsingStrategy> strategies) {

		Assert.notNull(charsetDetector, "charsetDetector must not be null");
		Assert.notNull(strategies, "strategies must not be null");
		Assert.notEmpty(strategies, "strategies must not be empty");

		this.strategies  = strategies;
		this.charsetDetector = charsetDetector;
		this.supportedExtensions = List.copyOf(strategies.stream()
			.map(FileParsingStrategy::supportedExtension)
			.collect(Collectors.toList()));
	}

	public List<String> supportedExtensions() {

		return supportedExtensions;
	}

	public ParsedFileResponseDto parse(File file) throws IOException {

		final var fileParsingStrategy = strategies.stream()
			.filter(strategy -> strategy.applies(file))
			.findFirst()
			.orElseThrow(() -> new IllegalStateException("No strategy applicable to file " + file.getName()));

		final var charset = charsetDetector.detect(file);

		final ParsedFileResponseDto response = fileParsingStrategy.parse(file, charset);
		deleteQuietly(file);

		return response;
	}
}
