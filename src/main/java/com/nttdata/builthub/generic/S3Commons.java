package com.nttdata.builthub.generic;

import com.amazonaws.regions.Regions;

public final class S3Commons {
    private S3Commons() {
    }

    public static final Regions S3_REGION = Regions.EU_CENTRAL_1;
    public static final String S3_RAWBOX_BUCKET = "builthub-rawbox";
    public static final String S3_FEEDBACK_BUCKET = "builthub-feedback";
}
