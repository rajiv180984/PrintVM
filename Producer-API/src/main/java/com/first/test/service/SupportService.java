package com.first.test.service;

import java.util.List;

import com.first.test.model.ContributorRoleRequest;
import com.first.test.model.Feedback;

public interface SupportService {
	
	public String feedBackSubmit(Feedback feedback);

	public  List<Feedback> getFeedbackListByEmailId(String emailId);

	public String contributorRoleRequest(ContributorRoleRequest request);

}
