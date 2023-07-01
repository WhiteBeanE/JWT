package com.bsm.JwtTest.damain;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtil {
	
	@SuppressWarnings("deprecation")
	public boolean isExpired(String token) {
		// Token 만료시간이 지났는지 확인 
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
				.getBody().getExpiration().before(new Date());
	}
	
	@SuppressWarnings("deprecation")
	public String getUserName(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
				.getBody().get("userName", String.class);
	}
	
	private final Key key;
	private Long expiredMs = 1000 * 60 * 60L;
	private String secretKey;
	
	public JwtUtil(@Value("${jwt.secret}") String secretKey) {
		log.info("[JwtProvider] secretKey : {}", secretKey);
		byte[] ketBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(ketBytes);
		this.secretKey = secretKey;
	}
	
	public String createJwt(String userName) {
		Claims claims = Jwts.claims();
		claims.put("userName", userName);
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
}
