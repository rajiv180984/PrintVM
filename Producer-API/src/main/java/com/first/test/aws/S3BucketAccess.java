package com.first.test.aws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cloudformation.model.AlreadyExistsException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetBucketLocationRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.SSEAlgorithm;
import com.first.test.model.ShareDocForMachineByProfessionals;

@Component
public class S3BucketAccess {

	private static Logger logger = LoggerFactory.getLogger(S3BucketAccess.class);
	private static long expTimeMillisForURL = 1000 * 60 * 60 * 24;

	@Value("${AWS.S3.AWSAccessKeyId}")
	private String AWSAccessKeyId = "AKIA47EYQMCX445PIR6I";
	//"AKIAJ6UOGEVLMBTDOYHQ";

	@Value("${AWS.S3.AWSSecretKey}")
	private String AWSSecretKey = "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";
	//"zvW00EmWTJ6vgvgECIN9Rv9rshbdN+er5HYSFZ+s";

	@Value("${AWS.S3.bucketName}")
	private String bucketName;

	@Value("${AWS.S3.bucketNameForSharedDoc}")
	private String bucketNameForSharedDoc;

	@Value("${AWS.S3.publicDocS3Bucket}")
	public String publicDocS3Bucket;

	@Value("${AWS.S3.directLoad.presignedexpiration}")
	public long presignedexpiration;

	public AmazonS3 s3client;

	S3BucketAccess() {
		AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKey);

		s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_2).withRegion(Regions.AP_SOUTH_1).enableForceGlobalBucketAccess().build();

		this.bucketName = "zeroxvm";

	}

	public static void main(String[] args) {

		System.out.println("================= S3 Bucket ============");
		
	}

	public void AWSS3Function(String machineId, String mobile, String securitycode) {

		String bucketName = "zeroxvm";

		System.out
				.println("============ =====================================" + s3client.getBucketLocation(bucketName));
		List<Bucket> list = s3client.listBuckets();
		System.out.println(" Total count of Bucket " + list.size());
		for (Bucket bucket : list) {
			System.out.println(" Bucket Name :" + bucket.getName());
		}

		if (s3client.doesBucketExist(bucketName)) {
			System.out.println(" Bucket Exist");
			return;
		} else {
			System.out.println(" Bucket does not Exist");
		}

		s3client.createBucket(bucketName);

	}

	// DocName = fileName
	public String createFolder(String machineId, String emailId, String printJobId, String docName) {

		// Evaluate path where file persist
		String s3PathforDoc = getS3PathFromMachineIdMobile(machineId, emailId, printJobId, docName);

		logger.info("AWS S3 File Location :" + s3PathforDoc);
		// create meta-data for your folder and set content-length to 0
		boolean exists = s3client.doesObjectExist(bucketName, s3PathforDoc);
		if (exists) {
			Random rand = new Random();
			int n = rand.nextInt(10);
			System.out.println(" Folder already Exist");
			// return s3BucketName.toString();
			s3PathforDoc = s3PathforDoc.concat(String.valueOf(n));
		}

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3PathforDoc, emptyContent, metadata);
		// send request to S3 to create folder
		PutObjectResult result = s3client.putObject(putObjectRequest);

		return s3PathforDoc;
	}

	public String getBucketName(String machineId, String mobileNo, String securityCode) {
		// Read from Property file
		try {

			if (!s3client.doesBucketExist(bucketName)) {
				// Because the CreateBucketRequest object doesn't specify a region, the
				// bucket is created in the region specified in the client.
				s3client.createBucket(new CreateBucketRequest(bucketName));

				// Verify that the bucket was created by retrieving it and checking its
				// location.
				String bucketLocation = s3client.getBucketLocation(new GetBucketLocationRequest(bucketName));
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

	private String getS3PathFromMachineIdMobile(String machineId, String mobileNo, String securityCode,
			String docName) {
		StringBuilder s3pathBuilder = new StringBuilder();
		String S3Path = s3pathBuilder.append(machineId).append("/").append(mobileNo).append("/").append(securityCode)
				.append("/").append(docName).toString();
		System.out.println("================= S3 Path :" + S3Path);
		return S3Path;
	}

	public String persistFile(String machineId, String emailId, String printJobId, MultipartFile file) {
		String s3path = null;
		try {

			// create empty content
			// create a PutObjectRequest passing the folder name suffixed by /
			String fileName = file.getOriginalFilename();
			System.out.println(" File name :" + fileName);
			s3path = createFolder(machineId, emailId, printJobId, fileName);
			ObjectMetadata metadata = new ObjectMetadata();

			System.out.println("persistFile :" + s3path);
			InputStream is = file.getInputStream();
			metadata.setContentLength(is.available());
			System.out.println("Size of File :" + is.available());

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3path, is, metadata);

			s3client.putObject(putObjectRequest);

			// send request to S3 to create folder
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return s3path;
	}

	public void awsDeleteFile(String s3path) {
		s3client.deleteObject(new DeleteObjectRequest(bucketName, s3path));
	}

	public ShareDocForMachineByProfessionals persistSharedDocOnMachineSpecific(
			ShareDocForMachineByProfessionals shareDoc, String machineId, String categoryName,
			String emailIdOfcontributor, String docName, MultipartFile file) {

		String s3path = null;
		try {
			logger.info(" File Details ============= {} \n {} \n {} \n {}", machineId, categoryName,
					emailIdOfcontributor, docName);
			StringBuilder s3pathBuilder = new StringBuilder();
			s3path = s3pathBuilder.append(machineId).append("/").append(categoryName).append("/").append(docName)
					.toString();
			// create empty content
			// create a PutObjectRequest passing the folder name suffixed by /
			String fileName = file.getOriginalFilename();
			System.out.println(" File name :" + fileName);
			ObjectMetadata metadata = new ObjectMetadata();

			System.out.println("persistFile :" + s3path);
			InputStream is = file.getInputStream();
			metadata.setContentLength(is.available());

			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketNameForSharedDoc, s3path, is, metadata);
			PutObjectResult result = s3client.putObject(putObjectRequest);
			logger.info("========================= Object Persist Successfully ");
			// create PreSigned URL for Download

			URL preSigneURL = getS3PreSignedURL(bucketNameForSharedDoc, s3path, presignedexpiration);
			logger.info("=========================Pre-Signed URL: {} ", preSigneURL.toString());
			shareDoc.setFullDownloadPath(preSigneURL.toString());
			shareDoc.setS3Path(preSigneURL.toString());
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(" Failed to publish :", e);
		}
		return shareDoc;

	}

	public String persistPublicDoc(MultipartFile file, String category) {
		StringBuilder s3path = null;
		try {

			// create empty content
			// create a PutObjectRequest passing the folder name suffixed by /
			String fileName = file.getOriginalFilename();
			s3path = new StringBuilder(category);
			s3path.append("/").append(fileName);
			System.out.println(" File name :" + fileName);
			boolean exists = s3client.doesObjectExist(bucketName, s3path.toString());
			if (exists) {
				// Apped some number and load
				throw new AlreadyExistsException("File Name already Exist");
			}
			ObjectMetadata metadata = new ObjectMetadata();

			System.out.println("persistFile :" + s3path);
			InputStream is = file.getInputStream();
			metadata.setContentLength(is.available());
			System.out.println("Size of File :" + is.available());

			PutObjectRequest putObjectRequest = new PutObjectRequest(publicDocS3Bucket, s3path.toString(), is,
					metadata);

			s3client.putObject(putObjectRequest);
			return s3path.toString();
			// send request to S3 to create folder
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return s3path.toString();
	}

	public String getAWSS3FilePath(String key) {

		S3Object object = s3client.getObject(new GetObjectRequest(bucketName, key));
		// Set the presigned URL to expire after one hour.
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += expTimeMillisForURL;
		expiration.setTime(expTimeMillis);

		// Generate the presigned URL.
		System.out.println("Generating pre-signed URL.");
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
				.withMethod(HttpMethod.GET).withExpiration(expiration);
		URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

		return url.toString();

	}

	public URL getS3PreSignedURL(String bucktName, String s3path, long expirationTime) {
		try {

			logger.info("========== S3Path of Persisted Doc {}", s3path);
			// Set the pre-signed URL to expire after one hour.
			java.util.Date expiration = new java.util.Date();

			long expTimeMillis = expiration.getTime();
			// 10 Years
			expTimeMillis += expirationTime;
			expiration.setTime(expTimeMillis);

			// Generate the pre-signed URL.
			System.out.println("Generating pre-signed URL.");
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucktName, s3path)
					.withMethod(HttpMethod.GET).withExpiration(expiration)
					.withContentType(MediaType.MULTIPART_FORM_DATA_VALUE);

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

			// Check to make sure that the object was uploaded successfully.
			return url;
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
		return null;
	}

	public URL getS3PreSignedURLForUpload(String bucktName, String s3path, long expirationTime) {
		try {

			logger.info("========== S3Path of Persisted Doc {}", s3path);
			// Set the pre-signed URL to expire after one hour.
			java.util.Date expiration = new java.util.Date();

			long expTimeMillis = expiration.getTime();
			// 10 Years
			expTimeMillis += presignedexpiration;
			expiration.setTime(expTimeMillis);

			// Generate the pre-signed URL.
			System.out.println("Generating pre-signed URL.");
			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucktName, s3path)
					.withMethod(HttpMethod.PUT).withExpiration(expiration)
					.withContentType(MediaType.MULTIPART_FORM_DATA_VALUE)
					.withSSEAlgorithm(SSEAlgorithm.KMS.getAlgorithm())
					;
					
					

			URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
try {
	System.out.println(" URL ==============="+url.toString() +"\n"+url.toURI().toString());
} catch (URISyntaxException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
			// Check to make sure that the object was uploaded successfully.
			return url;
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
		}
		return null;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public long getPresignedexpiration() {
		return presignedexpiration;
	}

	public void setPresignedexpiration(long presignedexpiration) {
		this.presignedexpiration = presignedexpiration;
	}

}
