package com.nttdata.builthub.generic.feedback;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.nttdata.builthub.generic.S3Commons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.nttdata.builthub.generic.S3Commons.S3_FEEDBACK_BUCKET;

@CrossOrigin
@RestController
public class FeedbackController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    private static final String KEY_DELIMITER = "-";
    private static final String KEY_TYPE_TEXT = "TEXT";
    private static final String KEY_TYPE_IMG_NAME = "IMG";
    private static final String KEY_TYPE_IMG_B64 = "B64";

    @PostMapping("/feedback")
    public ResponseEntity<String> submitFeedback(@RequestBody Feedback feedback) {
        var keyPrefix = this.getKeyPrefix(feedback.getSection());
        var amazonS3 = this.getAmazonS3Client();

        var keyText = keyPrefix + KEY_DELIMITER + KEY_TYPE_TEXT;
        amazonS3.putObject(S3_FEEDBACK_BUCKET, keyText, feedback.getFeedbackText());
        LOGGER.info("Text has been successfully saved.");

        if (this.feedbackContainsImage(feedback)) {
            var keyImgName = keyPrefix + KEY_DELIMITER + KEY_TYPE_IMG_NAME;
            amazonS3.putObject(S3_FEEDBACK_BUCKET, keyImgName, feedback.getImageName());

            var keyImgBase64 = keyPrefix + KEY_DELIMITER + KEY_TYPE_IMG_B64;
            amazonS3.putObject(S3_FEEDBACK_BUCKET, keyImgBase64, feedback.getImageBase64());

            LOGGER.info("Image and metadata have been successfully saved.");
        }

        return ResponseEntity.ok(keyPrefix);
    }

    private boolean feedbackContainsImage(final Feedback feedback) {
        return feedback.getImageName() != null
                && !feedback.getImageName().isEmpty()
                && feedback.getImageBase64() != null
                && !feedback.getImageBase64().isEmpty();
    }

    private String getKeyPrefix(String section) {
        String id = UUID.randomUUID().toString();
        return id + KEY_DELIMITER + section;
    }

    private AmazonS3 getAmazonS3Client() {
        return AmazonS3ClientBuilder
                .standard()
                .withRegion(S3Commons.S3_REGION)
                .build();
    }
}
