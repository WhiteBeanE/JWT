package com.bsm.JwtTest.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bsm.JwtTest.damain.JwtUtil;
import com.bsm.JwtTest.handler.JwtFilter;
import com.bsm.JwtTest.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	private final JwtUtil jwtUtil;
	private final UserService userService;
	
	// JWT용 시큐리티 재설정 기존 시큐리티 하단 주석처리
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.httpBasic().disable() // HTTP 기본 인증을 비활성화
			.csrf().disable() // csrf 보호 기능을 비활성화
	    	// 인증 허용 범위 설정
	    	.authorizeRequests()
		    	.antMatchers("/api/v1/users/login").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/**").authenticated()
				.anyRequest().authenticated()
				.and()
			// JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				// 보안 필터 체인에 사용자 정의 필터를 추가하는 역할
				// 기본 인증 필터 중 하나인 UsernamePasswordAuthenticationFilter 이전에 사용자 정의 필터인 JwtFilter를 실행하도록 설정
			.addFilterBefore(new JwtFilter(jwtUtil, userService), UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	
	@Bean
	// JWT를 사용하기 위해 기본적인 password encoder가 필요
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

}
