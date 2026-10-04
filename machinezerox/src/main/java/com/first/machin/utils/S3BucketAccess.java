package com.first.machin.utils;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;

@Component
public class S3BucketAccess {

	@Value("${AWS.S3.AWSAccessKeyId}")
	private String AWSAccessKeyId = "AKIAJ6UOGEVLMBTDOYHQ";

	@Value("${AWS.S3.AWSSecretKey}")
	private String AWSSecretKey = "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";

	@Value("${AWS.S3.bucketName}")
	private String bucketName;

	@Value("${machine.localbasepath}")
	private String localbasepath;

	private AmazonS3 s3client;

	private TransferManager xfer_mgr;

	S3BucketAccess() {
		AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKey);

		s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_2).build();

		this.bucketName = "zeroxvm";

	}

	public static void main(String[] args) {

		System.out.println("================= S3 Bucket ============");
	}

	private String getS3PathFromMachineIdMobile(String machineId, String mobileNo, String securityCode,
			String docName) {
		StringBuilder s3pathBuilder = new StringBuilder();
		String S3Path = s3pathBuilder.append(machineId).append("/").append(mobileNo).append("/").append(securityCode)
				.append("/").append(docName).toString();
		System.out.println("================= S3 Path :" + S3Path);
		return S3Path;
	}

	public String downloadFile(String s3PathURL, String localDocURL) {
		try {
			TransferManager xfer_mgr = TransferManagerBuilder.standard().withS3Client(s3client).build();
			
			URI fileToBeDownloaded = new URI(s3PathURL);
			AmazonS3URI s3URI = new AmazonS3URI(fileToBeDownloaded);

			S3Object s3Object = s3client.getObject(bucketName, s3URI.getKey());
			S3ObjectInputStream s3is = s3Object.getObjectContent();

			//
			Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rwxrwxrwx");
			FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions.asFileAttribute(perms);
			Path path = Paths.get(localDocURL);
			File localFile = path.toFile();

			Files.copy(s3is, path, StandardCopyOption.REPLACE_EXISTING);

			System.out.println("=== S3bucket  key=====" + s3URI.getKey() + "========== " + path.toString());
			Download xfer = xfer_mgr.download(bucketName, s3URI.getKey(), localFile);

			// download(bucketName, s3URI.getKey(), path);
			// loop with Transfer.isDone()
			xfer.waitForCompletion();
			/*
			 * while(! xfer.isDone()) { System.out.println();
			 * 
			 * }
			 */
			return localDocURL;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return localDocURL;
	}

	public void awsDeleteFile(String s3path) {
		s3client.deleteObject(new DeleteObjectRequest(bucketName, s3path));
	}

}
