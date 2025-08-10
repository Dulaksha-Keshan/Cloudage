package com.keshan.cloudage.org.model;



public enum ITYPE {
    JPEG("image/jpeg"),
    PNG("image/png"),
    WEBP("image/webp"),
    GIF("image/gif");

    private final String imageType;

    ITYPE(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }
}
