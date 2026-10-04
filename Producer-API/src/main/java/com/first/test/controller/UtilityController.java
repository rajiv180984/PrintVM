package com.first.test.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.first.test.util.FileConversionUtility;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.ServiceStatus;

@RestController
@RequestMapping(value = "/first/utility")
public class UtilityController {

	@GetMapping("/docTopdf")
	public ResponseEntity<GenericResponseEntity<String>> docToPdf() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("docTopdf"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);
	}

	@GetMapping("/pngTopdf")
	public ResponseEntity<GenericResponseEntity<String>> pngToPdf() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("pngTopdf"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);
	}

	@GetMapping("/jpgTopdf")
	public ResponseEntity<GenericResponseEntity<String>> jpgToPdf() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("jpgTopdf"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);
	}

	@GetMapping("/pptToPdf")
	public ResponseEntity<GenericResponseEntity<String>> pptToPdf() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("pptToPdf"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);
	}

	@GetMapping("/mergeFile")
	public ResponseEntity<GenericResponseEntity<String>> pdfMerge() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("mergeFile"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);

	}

	@GetMapping("/splitFile")
	public ResponseEntity<GenericResponseEntity<String>> splitFile() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("splitFile"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);

	}

	@GetMapping("/ocr")
	public ResponseEntity<GenericResponseEntity<String>> ocr() {
		GenericResponseEntity<String> result = new GenericResponseEntity<String>(
				FileConversionUtility.getUtilityURLbyType("ocr"), ServiceStatus.SUCCESS.name(), 200);
		return new ResponseEntity<GenericResponseEntity<String>>(result, HttpStatus.OK);

	}

}
