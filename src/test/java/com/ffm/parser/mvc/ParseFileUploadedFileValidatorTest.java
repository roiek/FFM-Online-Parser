/*
 * Created on 25-02-2019 18:13 by trojek
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

import static com.ffm.parser.mvc.ParseFileUploadedFileValidator.EMPTY_FILE_VALIDATION_MESSAGE;
import static com.ffm.parser.mvc.ParseFileUploadedFileValidator.NULL_FILE_VALIDATION_MESSAGE;
import static com.ffm.parser.mvc.ParseFileUploadedFileValidator.UNSUPPORTED_EXTENSION_VALIDATION_MESSAGE_TEMPLATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import com.ffm.parser.logic.fileparser.SupportedExtensionsProvider;


public class ParseFileUploadedFileValidatorTest {

	private ParseFileUploadedFileValidator validator;

	private static final String SUPPORTED_EXTENSION = "csv";
	private static final String SUPPORTED_EXTENSION_FILENAME = "test." + SUPPORTED_EXTENSION;
	private static final String UNSUPPORTED_EXTENSION_FILENAME = "test.prn";

	@Before
	public void setUp() {

		SupportedExtensionsProvider supportedExtensionsProvider = mock(SupportedExtensionsProvider.class);

		given(supportedExtensionsProvider.supportedExtensions()).willReturn(List.of(SUPPORTED_EXTENSION));
		given(supportedExtensionsProvider.supportedExtensionsText()).willReturn(SUPPORTED_EXTENSION);

		this.validator = new ParseFileUploadedFileValidator(supportedExtensionsProvider);
	}

	@Test
	public void shouldRejectNullFile() {

		//given
		MultipartFile file = null;

		//when
		var result = validator.validate(file);

		//then
		assertThat(result.hasErrors()).isTrue();
		assertThat(result.getErrors()).containsExactly(NULL_FILE_VALIDATION_MESSAGE);
	}

	@Test
	public void shouldRejectEmptyFile() {

		//given
		MultipartFile file = mock(MultipartFile.class);
		given(file.isEmpty()).willReturn(true);
		given(file.getOriginalFilename()).willReturn(SUPPORTED_EXTENSION_FILENAME);

		//when
		var result = validator.validate(file);

		//then
		assertThat(result.hasErrors()).isTrue();
		assertThat(result.getErrors()).containsExactly(EMPTY_FILE_VALIDATION_MESSAGE);
	}

	@Test
	public void shouldRejectFileWithUnsupportedExtension() {

		//given

		MultipartFile file = mock(MultipartFile.class);
		given(file.isEmpty()).willReturn(false);
		given(file.getOriginalFilename()).willReturn(UNSUPPORTED_EXTENSION_FILENAME);

		//when
		var result = validator.validate(file);

		//then
		assertThat(result.hasErrors()).isTrue();
		var expectedExtensionValidationMessage = String.format(UNSUPPORTED_EXTENSION_VALIDATION_MESSAGE_TEMPLATE, UNSUPPORTED_EXTENSION_FILENAME, SUPPORTED_EXTENSION);
		assertThat(result.getErrors()).containsExactly(expectedExtensionValidationMessage);
	}

	@Test
	public void shouldRejectEmptyFileWithUnsupportedExtension() {

		//given
		MultipartFile file = mock(MultipartFile.class);
		given(file.isEmpty()).willReturn(true);
		given(file.getOriginalFilename()).willReturn(UNSUPPORTED_EXTENSION_FILENAME);

		//when
		var result = validator.validate(file);

		//then
		assertThat(result.hasErrors()).isTrue();
		var expectedExtensionValidationMessage = String.format(UNSUPPORTED_EXTENSION_VALIDATION_MESSAGE_TEMPLATE, UNSUPPORTED_EXTENSION_FILENAME, SUPPORTED_EXTENSION);
		assertThat(result.getErrors()).containsExactly(EMPTY_FILE_VALIDATION_MESSAGE, expectedExtensionValidationMessage);
	}

	@Test
	public void shouldAcceptNotEmptyFileWithSupportedExtension() {

		//given
		MultipartFile file = mock(MultipartFile.class);
		given(file.isEmpty()).willReturn(false);
		given(file.getOriginalFilename()).willReturn(SUPPORTED_EXTENSION_FILENAME);

		//when
		var result = validator.validate(file);

		//then
		assertThat(result.hasErrors()).isFalse();
		assertThat(result.getErrors()).isEmpty();
	}
}