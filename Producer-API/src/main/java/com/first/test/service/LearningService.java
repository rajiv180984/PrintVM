package com.first.test.service;

import java.util.List;

import com.first.test.model.Lecture;

public interface LearningService {
   public LearningVO getLearningVOByEmailId(String emailId);
   public List<Lecture> Lectures(String emailId, String subjectId, String teacherId);
   
}
