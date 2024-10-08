package com.nttdata.builthub.generic.feedback;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Feedback {
    private String section;
    private String feedbackText;
    private String imageBase64;
    private String imageName;


    public String getSection() {
        return section;
    }

    @JsonProperty("section")
    public void setSection(String section) {
        this.section = section;
    }

    public String getFeedbackText() {
        return feedbackText;
    }

    @JsonProperty("feedback-text")
    public void setFeedbackText(String feedbackText) {
        this.feedbackText = feedbackText;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    @JsonProperty("image-base64")
    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getImageName() {
        return imageName;
    }

    @JsonProperty("image-name")
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
