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

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ffm.parser.fileparser.FileParser;


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
	private String parseFile(@ModelAttribute File file, Model model) {



		return "parsedDataTable.html";
	}
}
