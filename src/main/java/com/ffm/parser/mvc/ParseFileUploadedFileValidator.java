/*
 * Created on 25-02-2019 17:59 by trojek
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

import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.commons.io.FilenameUtils.isExtension;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.ffm.parser.logic.fileparser.SupportedExtensionsProvider;


@Service
class ParseFileUploadedFileValidator {

	public static final String NULL_FILE_VALIDATION_MESSAGE = "File is required, got null";
	public static final String EMPTY_FILE_VALIDATION_MESSAGE = "File is empty";
	public static final String UNSUPPORTED_EXTENSION_VALIDATION_MESSAGE_TEMPLATE = "File %s has not supported extension. Currently supported extensions are %s";
	private final SupportedExtensionsProvider supportedExtensionsProvider;

	@Autowired
	ParseFileUploadedFileValidator(SupportedExtensionsProvider supportedExtensionsProvider) {

		Assert.notNull(supportedExtensionsProvider, "supportedExtensionsProvider must not be null");

		this.supportedExtensionsProvider = supportedExtensionsProvider;
	}

	ParseFileValidationResult validate(MultipartFile file) {

		if (isNull(file)) {

			return ParseFileValidationResult.error(List.of(NULL_FILE_VALIDATION_MESSAGE));
		}

		List<String> errors = new ArrayList<>();

		if (file.isEmpty()) {

			errors.add(EMPTY_FILE_VALIDATION_MESSAGE);
		}

		if (isNotSupportedExtension(file)) {

			errors.add(format(UNSUPPORTED_EXTENSION_VALIDATION_MESSAGE_TEMPLATE,
				file.getOriginalFilename(), supportedExtensionsProvider.supportedExtensionsText()));
		}

		return errors.isEmpty() ? ParseFileValidationResult.ok() : ParseFileValidationResult.error(errors);
	}

	private boolean isNotSupportedExtension(MultipartFile file) {

		var filename = file.getOriginalFilename();

		return supportedExtensionsProvider.supportedExtensions().stream()
			.noneMatch(extension -> isExtension(filename, extension));
	}
}
