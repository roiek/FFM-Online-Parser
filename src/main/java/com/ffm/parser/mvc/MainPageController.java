/*
 * Created on 21-02-2019 22:23 by trojek
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

import static java.io.File.createTempFile;
import static org.apache.commons.io.FileUtils.getTempDirectoryPath;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ffm.parser.logic.fileparser.FileParser;
import com.ffm.parser.logic.fileparser.ParsedFileResponseDto;


@Controller
@RequestMapping("/")
public class MainPageController {

	public static final String P_FILE = "file";

	private final FileParser fileParser;
	private final ParseFileUploadedFileValidator validator;

	@Autowired
	private MainPageController(FileParser fileParser, ParseFileUploadedFileValidator validator) {

		Assert.notNull(fileParser, "fileParser must not be null");
		Assert.notNull(validator, "validator must not be null");

		this.fileParser = fileParser;
		this.validator = validator;
	}

	@GetMapping
	private String mainPage() {

		return "index.html";
	}

	@PostMapping
	private String parseFile(@RequestParam(name = P_FILE) MultipartFile file, Model model) throws IOException {

		var validationResult = validator.validate(file);

		if (validationResult.hasErrors()) {

			model.addAttribute("errors", validationResult.getErrors());

			return "parsedDataTable-error.html";
		}

		final ParsedFileResponseDto parsedFile = parseFile(file);

		model.addAttribute("headerRow", parsedFile.getHeaderRow());
		model.addAttribute("dataRows", parsedFile.getDataRows());
		model.addAttribute("charset", parsedFile.getCharset().name());

		return "parsedDataTable.html";
	}

	private ParsedFileResponseDto parseFile(@RequestParam(name = P_FILE) MultipartFile file) throws IOException {

		File temp = createTempFile(getTempDirectoryPath(), file.getOriginalFilename());
		file.transferTo(temp);

		return fileParser.parse(temp);
	}

}
