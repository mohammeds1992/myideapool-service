package com.backend.services.myideapool.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.backend.services.myideapool.exceptions.BadJWTTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

    private String SECRET_KEY = "kzYatrjKxQwy";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public String extractRefreshToken(String token) {
        return (String) extractAllClaims(token).get("refresh_token");
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Integer extractUserId(String token) {
        return (Integer) extractAllClaims(token).get("user_id");
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new BadJWTTokenException();
        }
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
		claims.put("user_id", userDetails.getUser().getId());
		claims.put("refresh_token", userDetails.getUser().getRefresh_token());

        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

	public Boolean validateToken(String token, CustomUserDetails userDetails) {
		final String username = extractUsername(token);
		final String refreshToken = extractRefreshToken(token);

		return (refreshToken != null && userDetails.getUser().getRefresh_token() != null
				&& refreshToken.equals(userDetails.getUser().getRefresh_token())
				&& username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}