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

package com.ffm.parser;

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

import com.ffm.parser.fileparser.FileParser;
import com.ffm.parser.fileparser.ParsedFileResponseDto;


@Controller
@RequestMapping("/")
public class MainPageController {

	private final FileParser fileParser;

	@Autowired
	MainPageController(FileParser fileParser) {

		Assert.notNull(fileParser, "fileParser must not be null");

		this.fileParser = fileParser;
	}

	@GetMapping
	private String mainPage() {

		return "index.html";
	}

	@PostMapping
	private String parseFile(@RequestParam(name = "qqfile") MultipartFile file, Model model) throws IOException {

		File temp = createTempFile(getTempDirectoryPath(), file.getOriginalFilename());
		file.transferTo(temp);
		final ParsedFileResponseDto parsedFile = fileParser.parse(temp);

		model.addAttribute("headerRow", parsedFile.getHeaderRow());
		model.addAttribute("dataRows", parsedFile.getDataRows());
		model.addAttribute("charset", parsedFile.getCharset().name());

		return "parsedDataTable.html";
	}
}
