package org.linagora.linshare.storage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import org.linagora.linshare.core.common.FtpClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

public class StorageAwsImpl extends StorageAdapterClass {

	String accesskey = null;
	String secretkey = null;
	String bucketname = null;
	Properties properties = null;

	public StorageAwsImpl() {
		properties = new FtpClientConfiguration().getclientProperties();
		this.accesskey = properties.getProperty(SMConstants.ACCESS_KEY);
		this.secretkey = properties.getProperty(SMConstants.SECRET_KEY);
		this.bucketname = properties.getProperty(SMConstants.BUCKET_NAME);
	}

	private static final Logger logger = LoggerFactory.getLogger(StorageAwsImpl.class);

	public void DeleteFile(String specialId) {
		credentials = new BasicAWSCredentials(this.accesskey, this.secretkey);
		s3client = new AmazonS3Client(credentials);
		try{
			s3client.deleteObject(this.bucketname, specialId);
			logger.debug("File with this {} id deleted successfully" +specialId);
			new AccessClass().update(specialId);
		}
		catch (AmazonClientException e) {
			logger.error("Error with deleting file from amazon" + e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void GetAll(String bucketName, String folderName) {
		credentials = new BasicAWSCredentials(this.accesskey, this.secretkey);
		s3client = new AmazonS3Client(credentials);
		logger.debug("Listing objects");
		ObjectListing objectListing = s3client
				.listObjects(new ListObjectsRequest().withBucketName(bucketName).withPrefix("Some_Folder"));
		List<S3ObjectSummary> objList = null;
		if (objectListing != null) {
			objList = objectListing.getObjectSummaries();
		}
		int i = 1;
		for (S3ObjectSummary objectSummary : objList) {
			logger.debug(
					"id: {}, bucketName :{}, key :{}, eTag :{}, size :{}, lastModified :{}, storageClass :{}, owner :{}",
					i, objectSummary.getBucketName(), objectSummary.getKey(), objectSummary.getETag(),
					objectSummary.getSize(), objectSummary.getLastModified(), objectSummary.getStorageClass(),
					objectSummary.getOwner());
			i++;
		}

	}

	public void GetById(String bucketName, String folderName, String key, String targetFile) {
		logger.debug("getting object details for folderName :{}, key :{}", folderName, key);
		credentials = new BasicAWSCredentials(this.accesskey, this.secretkey);
		s3client = new AmazonS3Client(credentials);

		S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, folderName + "/" + key));
		if (s3object != null) {
			InputStream reader = new BufferedInputStream(s3object.getObjectContent());
			File tFile = new File(targetFile);
			OutputStream writer = null;
			try {
				writer = new BufferedOutputStream(new FileOutputStream(tFile));

				int read = -1;
				while ((read = reader.read()) != -1) {
					writer.write(read);
				}
				writer.flush();
				writer.close();
				reader.close();
			} catch (IOException e) {
				logger.error("Exception while processing: {}", e);
				e.printStackTrace();
			}

			logger.debug("file info :{}", s3object.getObjectMetadata().getContentType());
			// file = new File(s3object.getObjectContent());
		} else {
			logger.debug("Oops! file not found!");
		}
	}

	@Override
	public StorageAccessDTO sendFile(String filePath, String folderName, String key) throws FileNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}
}
