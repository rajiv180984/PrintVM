package com.first.test.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.first.test.model.Lecture;
import com.first.test.service.LearningService;
import com.first.test.service.LearningVO;

@Controller
@RequestMapping(value = "/first/learning")
public class MyLearningCotroller {

	private Logger logger = LoggerFactory.getLogger(SmartOfficeServiceController.class);
	
	@Autowired
	LearningService learningService;
	
	@GetMapping("/mylearning")
	public ResponseEntity<LearningVO> getMyLearning(@RequestParam(name="emailId") String emailId){
		LearningVO learnign = learningService.getLearningVOByEmailId(emailId);
		return new ResponseEntity<LearningVO>(learnign, HttpStatus.OK);
	}

	@GetMapping("/Lectures")
	public ResponseEntity<List<Lecture>> getMyLearning(@RequestParam(name="emailId") String emailId,
			@RequestParam(name="subjectId") String subjectId,
			@RequestParam(name="teacherId") String teacherId){
		List<Lecture> learnign = learningService.Lectures("", "", "");
		return new ResponseEntity<List<Lecture>>(learnign, HttpStatus.OK);
	}

	
}
