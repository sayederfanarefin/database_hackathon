package com.databasecourse.erfan.web.controller;


import com.databasecourse.erfan.Constants;
import com.databasecourse.erfan.service.CustomdbService;
import com.databasecourse.erfan.web.dto.CustomdbDto;
import com.databasecourse.erfan.web.util.CheckIfAdmin;
import com.databasecourse.erfan.web.util.PagerModel;
import com.databasecourse.erfan.web.util.StringTruncator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.databasecourse.erfan.Constants.UPLOAD_LOCATION_ADMIN;
import static com.databasecourse.erfan.Constants.UPLOAD_LOCATION_COMMON;


@Controller
@RequestMapping(value = {"/files"})
public class FileController {


	@GetMapping("/user/{fileName}")
	public ResponseEntity<Resource> serveFileUser(@PathVariable String fileName) {
		try {
			Path filePath = Paths.get( UPLOAD_LOCATION_COMMON).resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
						.body(resource);
			} else {
				// Handle file not found or unreadable.
			}
		} catch (IOException e) {
			// Handle exceptions.
		}

		// Return an appropriate response if the file can't be served.
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/admin/{fileName}")
	public ResponseEntity<Resource> serveFileAdmin(@PathVariable String fileName) {
		try {
			Path filePath = Paths.get( UPLOAD_LOCATION_ADMIN).resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + resource.getFilename())
						.body(resource);
			} else {
				// Handle file not found or unreadable.
			}
		} catch (IOException e) {
			// Handle exceptions.
		}

		// Return an appropriate response if the file can't be served.
		return ResponseEntity.notFound().build();
	}


}
