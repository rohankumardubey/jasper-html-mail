/*******************************************************************************
 * Copyright 2018, Julius Krah
 * by the @authors tag. See the LICENCE in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.juliuskrah.jasper.web.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.juliuskrah.jasper.report.ExportFormat;
import com.juliuskrah.jasper.report.ReportService;

import lombok.RequiredArgsConstructor;

/**
 * @author Julius Krah
 *
 */
@RestController
@RequiredArgsConstructor
public class ApplicationResource {
	private final ReportService reportService;

	@GetMapping("/{username}")
	public ResponseEntity<byte[]> report(@PathVariable String username,
			@RequestParam(defaultValue = "pdf") ExportFormat format) {
		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		String contentType = null;
		if (format == ExportFormat.PDF)	contentType = "application/pdf";
		else if (format == ExportFormat.XLSX) contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		byte[] bytes = reportService.generatePDFReport(format, "pdf_rest_resource", params);
		ContentDisposition contentDisposition = ContentDisposition.builder("inline")
				.filename(username + "." + format).build();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentDisposition(contentDisposition);
		return ResponseEntity //
				.ok() //
				.header("Content-Type", contentType + "; charset=UTF-8") //
				.headers(headers) //
				.body(bytes);
	}
}