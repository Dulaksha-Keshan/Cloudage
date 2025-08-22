package com.keshan.cloudage.org.service;

import com.keshan.cloudage.org.util.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private static final String TOKEN_TYPE = "token_type";
    private final PublicKey publicKey;
    private final PrivateKey privateKey;

    private final long accessExpiration = 20*60*1000;//20 mins
    private final long refreshExpiration = 24*60*60*1000;// 24hrs


    public JwtService() throws Exception {
        this.publicKey = KeyUtils.loadPublicKey("keys/local-only/public_key.pem");
        this.privateKey = KeyUtils.loadPrivateKey("keys/local-only/private_key.pem");
    }


    public String generateAccessToken(String userName){

        final Map<String,Object> claims = Map.of(TOKEN_TYPE,"ACCESS_TOKEN");
        return buildToken(userName,claims,accessExpiration);

    }

    public String generateRefreshToken(String userName){

        final Map<String,Object> claims = Map.of(TOKEN_TYPE,"REFRESH_TOKEN");
        return buildToken(userName,claims,refreshExpiration);


    }

    private String buildToken(String userName, Map<String, Object> claims, long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(this.privateKey)
                .compact();
    }


    public boolean isValidToken (String token , String expectedUsername){

        final String username = getUserName(token);

        return username.equals(expectedUsername) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private String getUserName(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {

        try {
            return Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            throw new RuntimeException(e);
        }
    }

    public String refreshAccessToken (final String refreshToken){

        final Claims claims = extractClaims(refreshToken);

        if(!"REFRESH_TOKEN".equals(claims.get(TOKEN_TYPE))){
            throw new RuntimeException("Invalid token Type");
        }

        if(isTokenExpired(refreshToken)){
            throw new RuntimeException("Refresh token expired");
        }
        String userName = claims.getSubject();

        return generateAccessToken(userName);
    }

}
