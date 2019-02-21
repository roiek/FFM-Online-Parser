/*
 * Created on 21-02-2019 23:10 by trojek
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

import static org.apache.commons.io.FileUtils.readFileToByteArray;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.mozilla.universalchardet.UniversalDetector;
import org.springframework.stereotype.Service;


@Service
class CharsetDetector {

	private final UniversalDetector universalDetector = new UniversalDetector();

	Charset detect(File file) throws IOException {

		universalDetector.reset();
		universalDetector.handleData(readFileToByteArray(file));
		universalDetector.dataEnd();

		return Charset.forName(universalDetector.getDetectedCharset());
	}
}
