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
	
	private final Key key;
	private Long expiredMs = 1000 * 60 * 60L;
	
	public JwtUtil(@Value("${jwt.secret}") String secretKey) {
		log.info("[JwtProvider] secretKey : {}", secretKey);
		byte[] ketBytes = Decoders.BASE64.decode(secretKey);
		this.key = Keys.hmacShaKeyFor(ketBytes);
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
