package com.first.test;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeneratePresignedUrlAndUploadObject {

    public static void main(String[] args) throws IOException {
    	uploadFile();

    }
    
    public static void uploadFile() {
    	try {
    	URL url = new URL("https://zeroxvm.s3-us-west-2.amazonaws.com/98876ds%2C98876ds?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Date=20200404T171357Z&X-Amz-SignedHeaders=content-type%3Bhost&X-Amz-Expires=3599&X-Amz-Credential=AKIAJ6UOGEVLMBTDOYHQ%2F20200404%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Signature=16d02139fa2836709fb718c1972e932983ce9de21cd6f40544e711333364ff67");
        File file = new File("/home/rajeev/Downloads/Flipkart - SDE-IV.pdf");
        //Create Connection
        HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
      /*  OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write("This text uploaded as an object via presigned URL.");
        out.close();*/
        
        BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
        int i;
        // read byte by byte until end of stream
        while ((i = bis.read()) > 0) {
    bos.write(i);
        }
        bos.flush();
        bis.close();
        bos.close();
        System.out.println("HTTP response code: " + connection.getResponseCode());
    }catch(Exception ex){
        System.out.println("Failed to Upload File");
    }

    	
    }
}

