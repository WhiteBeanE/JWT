package com.bsm.JwtTest.damain;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
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
		String username = String.valueOf(parseClaims(token).get("username"));
        log.info("getUsernameFormToken subject = {}", username);
        return username;
//		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
//				.getBody().get("userName", String.class);
	}
	
	@SuppressWarnings("deprecation")
	public String getRole(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
				.getBody().get("role", String.class);
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
	
	public String createJwt(MemberDto memberDto) {
		Claims claims = Jwts.claims();
		claims.put("userName", memberDto.getUsername());
		claims.put("role", memberDto.getRoles());
		
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiredMs))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	// 토근 정보를 검증하는 메소드
	public boolean vaildateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
			log.info("[JwtProvider vaildateToken] Invaild JWT : {}", e);
		} catch (ExpiredJwtException e) {
			log.info("[JwtProvider vaildateToken] Expired JWT : {}", e);
		} catch (UnsupportedJwtException e) {
			log.info("[JwtProvider vaildateToken] Unsupported JWT : {}", e);
		} catch (IllegalArgumentException e) {
			log.info("[JwtProvider vaildateToken] JWT claims String is empty : {}", e);
		}
		return false;
	}
	
	// JWT 토근을 복호화하여 토큰에 들어있는 정보를 꺼내는 메소드
	public Authentication getAuthentication(String accessToken) {
		// 토큰 복호화
		Claims claims = parseClaims(accessToken);
		
		if(claims.get("auth") == null) {
			throw new RuntimeException("권한 정보가 없는 토큰입니다.");
		}
		
		// 클레임에서 권한 정보 가져오기
		Collection<? extends GrantedAuthority> authoritis = 
				Arrays.stream(claims.get("auth").toString().split(","))
					.map(SimpleGrantedAuthority::new)
					.collect(Collectors.toList());
		
		// UserDetails 객체를 만들어 Authentication 리턴
		UserDetails principal = new User(claims.getSubject(), "", authoritis);
		return new UsernamePasswordAuthenticationToken(principal, "", authoritis);
	}
	
	private Claims parseClaims(String accessToken) {
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
	
}
