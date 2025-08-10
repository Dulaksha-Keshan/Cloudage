package com.keshan.cloudage.org.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public enum ITYPE {
    JPEG("image/jpeg","jpeg"),
    PNG("image/png","png"),
    WEBP("image/webp","webp"),
    GIF("image/gif","gif");

    @Getter
    private final String MIME;
    @Getter
    private final String format;

    private static final Map<String, ITYPE> MIME_MAP = new HashMap<>();
    private static final Map<String, ITYPE> FORMAT_MAP = new HashMap<>();


//    ITYPE(String imageType , String format) {
//        this.MIME = imageType;
//        this.format = format;
//    }




    static{
        for (ITYPE itype : ITYPE.values()){
            MIME_MAP.put(itype.getMIME(),itype);
            FORMAT_MAP.put(itype.format,itype);
        }
    }


    public static ITYPE fromMIME(String MIME){
        return MIME_MAP.get(MIME);
    }

    public static ITYPE fromFormat(String format){
        return FORMAT_MAP.get(format);
    }


}
