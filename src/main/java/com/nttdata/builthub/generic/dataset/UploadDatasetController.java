package com.nttdata.builthub.generic.dataset;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;

import static com.nttdata.builthub.generic.S3Commons.S3_RAWBOX_BUCKET;
import static com.nttdata.builthub.generic.S3Commons.S3_REGION;

@CrossOrigin
@RestController
public class UploadDatasetController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UploadDatasetController.class);

    @PostMapping("/upload_dataset")
    public ResponseEntity<String> uploadFile(@RequestParam("files") MultipartFile[] files,
                                             @RequestParam("dataset") Integer dataset) {
        String message = "";
        try {
            // Parameters verification
            if (dataset == null) {
                throw new IllegalArgumentException("A valid dataset number must be specified");
            } else if (dataset < 1) {
                throw new IllegalArgumentException("A valid dataset number must be specified");
            } else if (dataset > 999) {
                throw new IllegalArgumentException("A valid dataset number must be specified");
            }

            final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(S3_REGION).build();

            String dsKeyName = String.format("dataset%03d/", dataset);
            if (!s3.doesObjectExist(S3_RAWBOX_BUCKET, dsKeyName)) {
                throw new FileNotFoundException("Invalid dataset folder: " + dsKeyName);
            }
            // Multiple files upload
            for (MultipartFile file : files) {
                this.uploadS3File(s3, dsKeyName, file);
            }

            message = "Successful uploaded : " + dsKeyName;
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);

            message = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    private void uploadS3File(final AmazonS3 s3,
                              final String folderName,
                              final MultipartFile file) throws SdkClientException, IOException {
        String s3KeyName = folderName + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(file.getContentType());
        s3.putObject(S3_RAWBOX_BUCKET, s3KeyName, file.getInputStream(), metadata);
    }
}
