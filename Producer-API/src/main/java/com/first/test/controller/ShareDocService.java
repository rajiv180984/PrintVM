package com.first.test.controller;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.first.test.model.ShareDocForMachineByProfessionals;

public interface ShareDocService {

	List<ShareDocForMachineByProfessionals> allsharedDocOfContributor(String emailId);
	public void shareprintdocOnlocation(String machineIdList, String securecode, String docTitle,
			String emailIdOfcontributor, String categoryName, String subCategory, MultipartFile file) ;
	void shareprintdocOnlocation(ShareDocForMachineByProfessionals shareDocForMachineByProfessionals, MultipartFile file);
}
