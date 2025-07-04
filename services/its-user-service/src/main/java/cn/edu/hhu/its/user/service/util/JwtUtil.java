package cn.edu.hhu.its.user.service.util;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    // 密钥
    private static final String SECRET = "mySuperSecretKey1234567890!@#$%^&*"; // 必须 >= 256bit
    private static final Key SIGNING_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    // 默认过期时间（毫秒） 例如：1小时
    private static final long DEFAULT_EXPIRATION = 60 * 60 * 1000L;

    /**
     * 生成 token（默认过期时间）
     */
    public static String generateToken(Map<String, Object> claims) {
        return generateToken(claims, DEFAULT_EXPIRATION);
    }

    /**
     * 生成 token（指定过期时间）
     */
    public static String generateToken(Map<String, Object> claims, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setClaims(claims) // 自定义 claims
                .setIssuedAt(now)  // 签发时间
                .setExpiration(expiryDate) // 过期时间
                .signWith(SIGNING_KEY, SignatureAlgorithm.HS256) // 使用密钥和算法签名
                .compact();
    }

    /**
     * 解析 token，返回 claims
     */
    public static Claims parseToken(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(SIGNING_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 判断 token 是否过期
     */
    public static boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    /**
     * 获取指定 claim（如：username）
     */
    public static String getClaim(String token, String key) {
        Claims claims = parseToken(token);
        return String.valueOf(claims.get(key));
    }

}
