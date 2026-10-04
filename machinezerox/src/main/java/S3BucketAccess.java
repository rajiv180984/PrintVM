/*import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;

public class S3BucketAccess {

	static String AWSAccessKeyId = "AKIAJ6UOGEVLMBTDOYHQ";
	static String AWSSecretKey = "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";

	public static void main(String[] args) {
		System.out.println("================= S3 Bucket ============");

		AWSCredentials credentials = new BasicAWSCredentials(AWSAccessKeyId, AWSSecretKey);

		AmazonS3 s3client = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_WEST_2).build();

		String bucketName = "zeroxvm";

		System.out.println("============ =====================================" + s3client.getBucketLocation(bucketName));

		List<Bucket> list = s3client.listBuckets();
		System.out.println(" Total count of Bucket "+ list.size());
		for(Bucket bucket: list) {
			System.out.println(" Bucket Name :"+  bucket.getName());
		}
		if (s3client.doesBucketExist(bucketName)) {
			System.out.println(" Bucket Exist");
			return;
		} else {
			System.out.println(" Bucket does not Exist");
		}

		s3client.createBucket(bucketName);

	}
}
*/