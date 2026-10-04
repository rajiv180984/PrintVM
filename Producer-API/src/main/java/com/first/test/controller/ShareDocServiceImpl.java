package com.first.test.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.BulkOperations.BulkMode;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.appstream.model.ResourceNotAvailableException;
import com.first.test.aws.S3BucketAccess;
import com.first.test.exception.InvalidDataException;
import com.first.test.model.Customer;
import com.first.test.model.ShareDocForMachineByProfessionals;
import com.first.test.model.SharedDocForUser;
import com.first.test.service.CustomerProfileService;

@Service
public class ShareDocServiceImpl implements ShareDocService {
	private static Logger logger = LoggerFactory.getLogger(ShareDocServiceImpl.class);

	@Autowired
	private S3BucketAccess s3BucketAccess;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	CustomerProfileService customerProfileService;

	@Override
	public List<ShareDocForMachineByProfessionals> allsharedDocOfContributor(String emailId) {
		// TODO Auto-generated method stub
		try {
			Query query = new Query(Criteria.where("docOwnerEmailId").is(emailId));
			List<ShareDocForMachineByProfessionals> docList = mongoTemplate.find(query,
					ShareDocForMachineByProfessionals.class);
			return docList;
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(" Failed to fetch list of doc  ", e);
		}
		return null;
	}

	@Override
	public void shareprintdocOnlocation(String machineIdList, String securecode, String docTitle,
			String emailIdOfcontributor, String categoryName, String subCategory, MultipartFile file) {

		ShareDocForMachineByProfessionals shareDoc = new ShareDocForMachineByProfessionals();
		shareDoc.setDocOwnerEmailId(emailIdOfcontributor);

		String machineIds[] = machineIdList.split(",");

		if (machineIds.length == 0 || machineIds.length > 10) {
			throw new InvalidDataException(" Minimum One [1] MachineId is mandatory");
		}

		try {
			// persist doc in S3 Bucket
			shareDoc= s3BucketAccess.persistSharedDocOnMachineSpecific(shareDoc, machineIds[0], categoryName,
					emailIdOfcontributor, docTitle, file);
			// Evaluate Absolute downloadable path
			// machineId/categoryName/emailId/fileName

			
			StringBuilder pathBuilder = new StringBuilder(shareDoc.getFullDownloadPath());
			/*pathBuilder.append(s3BucketAccess.publicDocS3Bucket).append(".s3-")
					.append(s3BucketAccess.s3client.getRegionName()).append(".amazonaws.com/").append(s3path);
			*/
			String s3path = shareDoc.getS3Path();
			// Fetch Doc Owner Details by MobileNumber
			Customer cust = customerProfileService.getCustomerDetailByPhone(emailIdOfcontributor);
			if (cust == null) {
				throw new ResourceNotAvailableException("No Mobile Exist ");
			}
			// Then Public for this given machines when Machine Detected only given list
			// populated
			List<ShareDocForMachineByProfessionals> listDoc = new ArrayList<>();
			for (String machineId : machineIds) {
				shareDoc = new ShareDocForMachineByProfessionals();

				shareDoc.setCreatedOn(new Date());
				shareDoc.setMachineId(machineId);
				shareDoc.setDocOwnerEmailId(emailIdOfcontributor);
				shareDoc.setDocOwenerName(cust.getCustName());
				shareDoc.setDocTitle(docTitle);
				shareDoc.setFullDownloadPath(pathBuilder.toString());
				if (securecode == null || securecode.isEmpty()) {
					shareDoc.setScopeOfDoc("PUBLIC");
					shareDoc.setSecurecode("");
				} else {
					shareDoc.setScopeOfDoc("SECURED");
					shareDoc.setSecurecode(securecode);
				}
				// persist one copy and shared among all given machines
				shareDoc.setS3Path(s3path);
				shareDoc.setCategory(categoryName);
				// persit Shared doc
				listDoc.add(shareDoc);
				mongoTemplate.save(shareDoc);
				logger.info("One Record save {}", shareDoc);
			}

			/*
			 * BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED,
			 * ShareDocForMachineByProfessionals.class); bulkOperations.insert(listDoc);
			 * BulkOperations bulkOps = mongoTemplate.bulkOps(BulkMode.UNORDERED,
			 * SharedDocForUser.class);
			 */
			// To-Do
			// Publish DOC to Machine as well and Transfer this doc to Machine
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to persit file ", e);
		}

	}

	@Override
	public void shareprintdocOnlocation(ShareDocForMachineByProfessionals shareDocByProf, MultipartFile file) {
		ShareDocForMachineByProfessionals shareDoc = new ShareDocForMachineByProfessionals();
		shareDoc.setDocOwnerEmailId(shareDocByProf.getDocOwnerEmailId());

		String machineIds[] = shareDocByProf.getMachineIds().split(",");

		if (machineIds.length == 0 || machineIds.length > 10) {
			throw new InvalidDataException(" Minimum One [1] MachineId is mandatory");
		}

		logger.info("Recived Data : {}", shareDocByProf.toString());
		try {
			// persist doc in S3 Bucket
			shareDoc = s3BucketAccess.persistSharedDocOnMachineSpecific(shareDoc, machineIds[0], shareDocByProf.getCategory(),
					shareDocByProf.getDocOwnerEmailId(), shareDocByProf.getDocTitle(), file);
			// Evaluate Absolute downloadable path
			// machineId/categoryName/emailId/fileName

			logger.info("======Region Name===========" + s3BucketAccess.s3client.getRegionName());

			// Fetch Doc Owner Details by MobileNumber
			Customer cust = customerProfileService.getCustomerDetailByPhone(shareDocByProf.getDocOwnerEmailId());
			if (cust == null) {
				throw new ResourceNotAvailableException("No Mobile Exist ");
			}
			// Then Public for this given machines when Machine Detected only given list
			// populated
			List<ShareDocForMachineByProfessionals> listDoc = new ArrayList<>();
			for (String machineId : machineIds) {
				shareDoc = new ShareDocForMachineByProfessionals();

				shareDoc.setCreatedOn(new Date());
				shareDoc.setMachineId(machineId);
				shareDoc.setDocOwnerEmailId(shareDocByProf.getDocOwnerEmailId());
				shareDoc.setDocOwenerName(cust.getCustName());
				shareDoc.setDocTitle(shareDocByProf.getDocTitle());
			//	shareDoc.setFullDownloadPath(pathBuilder.toString());
				shareDoc.setSubCategory(shareDocByProf.getSubCategory());
				if (shareDocByProf.getSecurecode() == null ) {
					shareDoc.setScopeOfDoc("PUBLIC");
					shareDoc.setSecurecode("");
				} else {
					shareDoc.setScopeOfDoc("SECURED");
					shareDoc.setSecurecode(shareDocByProf.getSecurecode());
				}
				// persist one copy and shared among all given machines
				shareDoc.setCategory(shareDocByProf.getCategory());
				// persit Shared doc
				listDoc.add(shareDoc);
				mongoTemplate.save(shareDoc);
				logger.info("One Record save {}", shareDoc);
			}

			/*
			 * BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkMode.UNORDERED,
			 * ShareDocForMachineByProfessionals.class); bulkOperations.insert(listDoc);
			 * BulkOperations bulkOps = mongoTemplate.bulkOps(BulkMode.UNORDERED,
			 * SharedDocForUser.class);
			 */
			// To-Do
			// Publish DOC to Machine as well and Transfer this doc to Machine
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("Failed to persit file ", e);
			throw new InvalidDataException(" Document Failed to upload ");
		}

		
	}
}
