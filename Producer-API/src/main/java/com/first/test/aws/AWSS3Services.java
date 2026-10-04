/*package com.first.test.aws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;

@Component
public class AWSS3Services {

	private AmazonS3 s3Client;

	AWSS3Services() {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials("access_key_id", "secret_key_id");
		String clientRegion = "*** Client region ***";
		this.s3Client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(clientRegion).build();

	}

	public String getBucketName(String machineId) {
		String clientRegion = "*** Client region ***";
		// Read from Property file
		String bucketPrefix = "testMachine_";
		String bucketName = bucketPrefix.concat(machineId);
		try {

			if (!s3Client.doesBucketExist(bucketName)) {
				// Because the CreateBucketRequest object doesn't specify a region, the
				// bucket is created in the region specified in the client.
				s3Client.createBucket(new CreateBucketRequest(bucketName));

				// Verify that the bucket was created by retrieving it and checking its
				// location.
				String bucketLocation = s3Client.getBucketLocation(new GetBucketLocationRequest(bucketName));
				System.out.println("Bucket location: " + bucketLocation);
			}
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it and returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
		return bucketName;
	}


	public void persistFile(String machineId, String mobileNumber, String securityNumber, MultipartFile file) {
    	try {
    		
    	
		String bucketName = getBucketName(machineId);
    	StringBuilder s3FilePathSB = new StringBuilder();
    	String docName = file.getOriginalFilename();
    	s3FilePathSB = s3FilePathSB.append(mobileNumber).append(securityNumber).append(docName);
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.setContentLength(0);
    	// create empty content
    	// create a PutObjectRequest passing the folder name suffixed by /
    	String folderName = createFolder(machineId, mobileNumber);
    	
    	
    	InputStream is = file.getInputStream();
        
    	PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FilePathSB.toString()
    			, is,new ObjectMetadata());
    	
    	s3Client.putObject(putObjectRequest);
    	// send request to S3 to create folder
    	} catch (Exception e) {
			// TODO: handle exception
		}
    	
    }

	public String createFolder(String machineId, String mobileNumber) {
		String bucketName = getBucketName(machineId);
		StringBuilder s3FilePathSB = new StringBuilder();
		s3FilePathSB = s3FilePathSB.append(mobileNumber);
		// create meta-data for your folder and set content-length to 0
		boolean exists =s3Client.doesObjectExist(bucketName, s3FilePathSB.toString());
		if(exists) {
			System.out.println(" Folder already Exist");
			return s3FilePathSB.toString();
		}
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FilePathSB.toString(), emptyContent,
				metadata);
		// send request to S3 to create folder
		s3Client.putObject(putObjectRequest);
		return s3FilePathSB.toString();
	}
}*/