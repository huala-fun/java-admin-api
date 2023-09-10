package com.zeroonedance.adminapi.utils;


import com.zeroonedance.adminapi.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Component
public class JWTUtil {

    private static final String SECRET_KEY = "YrxPyhEzfgSUCjHCFdJofKxlNacBslWH";


    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, new HashMap<>());
    }

    public String generateToken(UserDetails userDetails, Map<String, Object> extractClaim) {
        final long signTime = System.currentTimeMillis();
        final long expirationTime = signTime + 1000 * 60 * 24;
        return Jwts.builder().setClaims(extractClaim)
                .setSubject(userDetails.getUsername())
                .setClaims(extractClaim)
                .setIssuedAt(new Date(signTime))
                .setExpiration(new Date(expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .toString();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (userName.equals( userDetails.getUsername()) && isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().
                setSigningKey(getSignInKey())
                .build().parseClaimsJws(token).getBody();
    }


    private javax.crypto.SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}
