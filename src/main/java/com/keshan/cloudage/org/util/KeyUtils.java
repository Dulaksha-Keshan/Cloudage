package com.keshan.cloudage.org.util;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtils {
    public KeyUtils() {
    }

    public static PrivateKey loadPrivateKey(String pemPath) throws Exception{

        final String key = readKeyFromResource(pemPath).replace("-----BEGIN PRIVATE KEY-----", "")
                                                        .replace("-----END PRIVATE KEY-----","")
                                                        .replaceAll("\\s+","");

        final byte[] decode = Base64.getDecoder().decode(key);

        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decode);

        return KeyFactory.getInstance("RSA").generatePrivate(spec);

    }


    public static PublicKey loadPublicKey(String pemPath) throws Exception{

        final String key = readKeyFromResource(pemPath).replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----","")
                .replaceAll("\\s+","");

        final byte[] decode = Base64.getDecoder().decode(key);

        final X509EncodedKeySpec spec = new X509EncodedKeySpec(decode);

        return KeyFactory.getInstance("RSA").generatePublic(spec);

    }

    private static String readKeyFromResource(String pemPath) throws IOException {
        try (final InputStream in = KeyUtils.class.getClassLoader().getResourceAsStream(pemPath)){
            if(in == null){
                throw new IllegalArgumentException("Could not find key in the given path " + pemPath);
            }
            return new String(in.readAllBytes());

        }
    }
}
