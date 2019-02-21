/*
 * Created on 21-02-2019 22:50 by trojek
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

import org.apache.commons.io.FilenameUtils;


interface FileParsingStrategy {

	default boolean applies(File file) {

		return FilenameUtils.isExtension(file.getName(), supportedExtension());
	}
	String supportedExtension();
	ParsedFileResponseDto parse(File file, Charset charset) throws IOException;
}
