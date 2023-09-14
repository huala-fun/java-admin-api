package com.zeroonedance.adminapi.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private static final String SECRET_KEY = "YrxPyhEzfgSUCjHCFdJofKxlNacBslWH";

    /**
     * 为给定的用户详细信息和声明映射生成令牌。
     *
     * @param uniqueIdentifier 用户唯一标识
     * @return 生成的令牌
     */
    public String generateToken(String uniqueIdentifier) {
        return generateToken(uniqueIdentifier, new HashMap<>());
    }

    /**
     * 为给定的用户详细信息和声明映射生成令牌。
     * <br/>
     * extractClaim 中存在 sub 、iat 等标准声明的话，会覆盖方法内标准声明
     * <br/>
     * {sub:11} 中的 11 会替换掉 uniqueIdentifier
     *
     * @param uniqueIdentifier 用户唯一标识
     * @param extractClaim     自定义声明（扩展声明）
     * @return 生成的令牌
     */
    public String generateToken(String uniqueIdentifier, Map<String, Object> extractClaim) {
        final long signTime = System.currentTimeMillis();
        final long expirationTime = signTime + 1000 * 60 * 24;
        return Jwts.builder()
                .setIssuer("admin-api") // 颁发者为 admin-api
                .setSubject(uniqueIdentifier) // 存放用户唯一标识
                .setIssuedAt(new Date(signTime)) // 存放签发时间
                .setExpiration(new Date(expirationTime))  // 存放过期时间
                .addClaims(extractClaim)            // 追加声明（扩展声明）
                .signWith(getSignInKey()).compact();
    }


    /**
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && isTokenExpired(token));
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
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

}
