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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


@Service
public class PrnFileParser implements FileParsingStrategy {

	private static final String PRN_EXTENSION = "prn";

	@Override
	public String supportedExtension() {

		return PRN_EXTENSION;
	}

	@Override
	public ParsedFileResponseDto parse(File file, Charset charset) throws IOException {

		final List<String> lines = FileUtils.readLines(file, charset);

		final var header = parseHeader(lines);

		return new ParsedFileResponseDto(header, Collections.emptyList());
	}

	private Row parseHeader(List<String> lines) {

		var data = lines.get(0);

		var list = new ArrayList<String>();

		List<Integer> starts = new ArrayList<>();
		starts.add(0);
		List<Integer> ends = new ArrayList<>();

		final Matcher matcher = Pattern.compile("\\s{2,}").matcher(data);
		while (matcher.find()) {
			starts.add(matcher.start());
			ends.add(matcher.end());
		}
		ends.add(data.length());

		Assert.state(starts.size() == ends.size());

		for (int i =0; i < starts.size(); i++) {
			list.add(data.substring(starts.get(i), ends.get(i)).trim());
		}

		final List<String> collect = list.stream().flatMap(potentialHeaderColumn -> trySplit(potentialHeaderColumn, lines))
			.collect(Collectors.toList());

		return new Row(collect);
	}

	private Stream<String> trySplit(String potentialHeaderColumn, List<String> lines) {

		if (potentialHeaderColumn.contains(" ")) {

			final Matcher matcher1 = Pattern.compile(potentialHeaderColumn).matcher(lines.get(0));
			final int start = matcher1.find()?matcher1.start() : 0;
			final var matcher = Pattern.compile("\\s").matcher(potentialHeaderColumn);
			while (matcher.find()) {

				if (lines.stream().allMatch(line -> line.charAt(start + matcher.start()) == ' ')) {

					return Stream.of(
						potentialHeaderColumn.substring(0, matcher.start()).trim(),
						potentialHeaderColumn.substring(matcher.start()).trim()
						);
				}
			}
			return Stream.of(potentialHeaderColumn);

		} else {

			return Stream.of(potentialHeaderColumn);
		}
	}
}
