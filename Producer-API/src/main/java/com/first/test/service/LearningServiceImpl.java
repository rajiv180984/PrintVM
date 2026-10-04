package com.first.test.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.first.test.model.Lecture;
import com.first.test.model.MySubject;

@Service
public class LearningServiceImpl implements LearningService{

	@Autowired
	MongoTemplate mtemplate;
	
	
	@Override
	public LearningVO getLearningVOByEmailId(String emailId) {
		// TODO Auto-generated method stub
		LearningVO learn = new LearningVO();
		
		List<MySubject> subjects = new ArrayList<>();
		MySubject class1 = new MySubject(123, "Class 4th Sec-B", "CLASS", "122", "Super Man");
		MySubject notice = new MySubject(123, "", "NOTICE", "122", "Super Man");
		
		MySubject sub1 = new MySubject(123, "Chemistry", "SUBJECT", "122", "Super Man");
		MySubject sub2 = new MySubject(123, "Physics", "SUBJECT", "122", "Chaman Prash");
		MySubject sub3 = new MySubject(123, "Mathematics", "SUBJECT", "122", "Baburao apte");
		MySubject sub4 = new MySubject(123, "Hindi", "SUBJECT", "122", "Super Man");
		
		
		subjects.add(class1);
		subjects.add(notice);
		subjects.add(sub1);
		subjects.add(sub2);
		subjects.add(sub3);
		subjects.add(sub4);
		
		learn.setSubjects(subjects);
		
		return learn;
	}


	@Override
	public List<Lecture> Lectures(String emailId, String subjectId, String teacherId) {
		// TODO Auto-generated method stub
		List<Lecture> lectures = new ArrayList<>();
		
		lectures.add(new Lecture("wyy","123", "3123","Organic Chemisry", null, "10-Aug-2020", null, null));
		lectures.add(new Lecture("wyy","123", "3123","Atom Theory", null, "10-Aug-2020", null, null));
		lectures.add(new Lecture("wyy","123", "3123","Molecules", null, "10-Aug-2020", null, null));
		lectures.add(new Lecture("wyy","123", "3123","Matters", null, "10-Aug-2020", null, null));
		lectures.add(new Lecture("wyy","123", "3123","Physical Chemisry", null, "10-Aug-2020", null, null));
		
		return lectures;
	}
	

}
