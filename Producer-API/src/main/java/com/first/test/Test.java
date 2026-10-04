package com.first.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class Test
{
	public AmazonS3 s3client;
    public static void main( String[] args )
    {
    	System.out.println(" Random Strig ========"+RandomStringUtils.randomAlphabetic(7));
    	System.out.println("============= "+ System.currentTimeMillis());
    	int i=20;
    	float t = 20.35f+20;
    	System.out.println("@@@@@@@     "+t);
    	LocalDateTime today =  LocalDateTime.now();  
    	LocalDateTime nextMonth = today.plusDays(30);
    	Timestamp timestamp = Timestamp.valueOf(nextMonth);
    	Test test = new Test();
    	test.fetchObjec("");
    	
    	
    }
    
    public static void testAPI() {
    	
    	  Map<String, String> params = new HashMap<>();
          params.put("grant_type", "password");
          params.put("client_id", "secret");
          params.put("username", "rajiv180984@gmail.com");
          params.put("password", "HZ6hsVh@");
          Response response = RestAssured.given()
            .auth().preemptive().basic("client_id", "secret")
            .and().with().params(params).when()
            .post("http://15.206.149.160:9000/oauth/token");
          System.out.println("========###=========="+response.getStatusCode());
          System.out.println("=================="+response.jsonPath().getString("access_token"));
    }
    
    public void fetchObjec(String path) {
    	AWSCredentials credentials = new BasicAWSCredentials("AKIASG4QNJ3YN4VORB5D", "tUGjiNvwFMS8BTdVqGX14H0SdOPNmRvIqKIBCwFi");
    	s3client = s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_WEST_2).withRegion(Regions.AP_SOUTH_1).enableForceGlobalBucketAccess().build();
    	
    	
    	S3Object fullObject = s3client.getObject(new GetObjectRequest("zeroxsharedoc", "123456/BANK/1.7.20 4th maths.pdf"));
        System.out.println("Content-Type: " + fullObject.getObjectMetadata().getContentType());
        System.out.println("Content: ");
        
        System.out.println("Downloaded S3 object: " + fullObject.getKey() + " from bucket " +fullObject.getBucketName());
        Path filePath = Paths.get("/home/rajeev/Test/myfile");
        S3ObjectInputStream stream = fullObject.getObjectContent();
        try {
			FileUtils.copyInputStreamToFile(stream, filePath.toFile());
			System.out.println("Completed ");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
       
        
        
    	
    }
    private static void displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println();
    }
}