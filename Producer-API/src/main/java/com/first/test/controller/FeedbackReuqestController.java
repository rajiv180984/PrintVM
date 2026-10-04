package com.first.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first.test.model.ContributorRoleRequest;
import com.first.test.model.Feedback;
import com.first.test.service.SupportService;
import com.first.test.util.GenericResponseEntity;
import com.first.test.util.ServiceStatus;

@Controller
@RequestMapping(value = "/first/support")
public class FeedbackReuqestController {

	@Autowired
	SupportService supportService;

	@PostMapping("/addFeedback")
	public ResponseEntity<GenericResponseEntity<String>> addFeedback(@RequestBody Feedback feedback) {
		String result = supportService.feedBackSubmit(feedback);
		return new ResponseEntity<GenericResponseEntity<String>>(
				new GenericResponseEntity<String>(result, ServiceStatus.SUCCESS.name(), 200), HttpStatus.OK);
	}

	@GetMapping("/getFeedbackListByEmailId")
	public ResponseEntity<GenericResponseEntity<List<Feedback>>> getFeedbackListByEmailId(
			@RequestParam(name = "emailId", required = true) String emailId) {
		List<Feedback> result = supportService.getFeedbackListByEmailId(emailId);
		
		return new ResponseEntity<GenericResponseEntity<List<Feedback>>>(
				new GenericResponseEntity<List<Feedback>>(result, ServiceStatus.SUCCESS.name(), 200), HttpStatus.OK);
	}

	//Request for contributor
	@PostMapping("/contributorRoleRequest")
	public ResponseEntity<GenericResponseEntity<String>> contributorRoleRequest(@RequestBody ContributorRoleRequest request){
		
		String result = supportService.contributorRoleRequest(request);
		return new ResponseEntity<GenericResponseEntity<String>>(
				new GenericResponseEntity<String>(result, ServiceStatus.SUCCESS.name(), 200), HttpStatus.OK);

	}
}
