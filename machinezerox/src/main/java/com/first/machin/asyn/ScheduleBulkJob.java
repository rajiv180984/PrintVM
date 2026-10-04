package com.first.machin.asyn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.first.machin.persistence.PersistRequestLocalService;
import com.first.machin.utils.S3BucketAccess;

import om.first.machin.model.PrintDocRequest;

@Service
public class ScheduleBulkJob {

	private static Logger log = LoggerFactory.getLogger(ScheduleBulkJob.class);

	private static int MAX_RETRY = 5;
	
	@Autowired
	S3BucketAccess s3BucketAccess;
	
	@Autowired 
	PersistRequestLocalService persistRequestLocalService;
	
	private void waitUntilNextTry()
    {
        try {
               Thread.sleep(500);
        }
        catch (InterruptedException iex) { }
    }


	@Async("bulkObAsyncExecutor")
	public void  startBacthJob(PrintDocRequest printDocRequest, String localDocURL) throws Exception 
	{
		// persist locally
		
		
		 localDocURL = s3BucketAccess.downloadFile(printDocRequest.getDocumentURL(), localDocURL);
		persistRequestLocalService.persistLocally(printDocRequest, localDocURL);
		
	}


}
